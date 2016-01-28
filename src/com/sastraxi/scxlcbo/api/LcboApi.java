package com.sastraxi.scxlcbo.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sastraxi.scxlcbo.exception.ApiException;
import com.sastraxi.scxlcbo.model.Beer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Collection of methods to interact with the LCBO API (lcboapi.com).
 */
public class LcboApi {

    private final String authHeader;
    private final Random random;

    public LcboApi(String accessKey) {
        this.authHeader = "Token " + accessKey;
        this.random = new Random();
    }

    /**
     * Performs status-checking on the response given from the LCBO API server.
     *
     * @param response
     * @throws ApiException upon any failure to communicate properly with the API.
     */
    private void validate(HttpResponse<JsonNode> response) throws ApiException {
        // deal with regular HTTP status codes
        if (response.getStatus() != 200) {
            throw new ApiException("HTTP error, status=" + response.getStatus() +
                    ", message=" + response.getStatusText());
        }

        // the LCBO API additionally has its own response codes inside of JSON
        JSONObject root = response.getBody().getObject();
        if (root.getInt("status") != 200) {
            throw new ApiException("LCBO returned error, status=" + root.getInt("status") +
                    ", message=" + root.getString("message"));
        }
    }

    /**
     * Performs an inventory check -- is the given product available right now at the given store?
     *
     * @param storeId LCBO store identifier.
     * @param productNumber LCBO product number.
     * @return true if there is at least one of the product available at the store.
     * @throws ApiException upon any failure to communicate properly with the API.
     */
    public boolean isProductAvailable(int storeId, int productNumber) throws ApiException {
        try {
            HttpResponse<JsonNode> response = Unirest.get("http://lcboapi.com/stores/{store_id}/products/{product_id}/inventory")
                .header("Authorization", authHeader)
                .header("accept", "application/json")
                .routeParam("store_id", String.valueOf(storeId))
                .routeParam("product_id", String.valueOf(productNumber))
                .asJson();

            validate(response);

            JSONObject result = response.getBody().getObject().getJSONObject("result");
            return result.getInt("quantity") > 0;

        } catch (UnirestException e) {
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * Builds a list of the integers in the given range and in a random order.
     */
    private List<Integer> permutedRange(int startInclusive, int endInclusive) {
        List<Integer> range = IntStream.rangeClosed(startInclusive, endInclusive).boxed().collect(Collectors.toList());
        Collections.shuffle(range);
        return range;
    }

    /**
     * Grabs a page of products that have at one point been at a given store.
     */
    private HttpResponse<JsonNode> doProductSearch(int storeId, int pageNumber) throws UnirestException {
        return Unirest.get("http://lcboapi.com/products")
            .header("Authorization", authHeader)
            .header("accept", "application/json")
            .queryString("q", "beer")
            .queryString("store_id", storeId) // N.B. returns any product that has *ever* been at this store
            .queryString("page", pageNumber)
            .asJson();
    }

    /**
     *
     * By passing in a predicate that tells us whether or not a candidaate beer can be chosen, we can reduce the
     * number of
     *
     * @param storeId
     * @param isProductNumberInHistory
     * @return
     * @throws ApiException
     */
    public Beer getNewRandomAvailableBeer(int storeId, SeenProductPredicate isProductNumberInHistory) throws ApiException {
        try {
            // do an initial search for the term "beer". we'll only use this page to determine # of matching products
            // FIXME couldn't find a way to directly query primary_category=Beer, so we check it after for correctness
            HttpResponse<JsonNode> response = doProductSearch(storeId, 1);
            validate(response);

            // generate a random permutation of page numbers [1..total pages]
            JSONObject pager = response.getBody().getObject().getJSONObject("pager");
            int totalPages = pager.getInt("total_pages");
            List<Integer> pageNumbers = permutedRange(1, totalPages);

            // keep picking random pages, grabbing all products on that page
            for (int pageNumber: pageNumbers) {

                response = doProductSearch(storeId, pageNumber);
                validate(response);

                // go through each item on the page (in random order) until we find a beer we haven't seen
                JSONArray results = response.getBody().getObject().getJSONArray("result");
                List<Integer> resultIndices = permutedRange(0, results.length() - 1);
                for (int i: resultIndices) {
                    JSONObject result = results.getJSONObject(i);
                    int productNumber = result.getInt("product_no");
                    boolean seenProduct = false;

                    // allow exceptions in our predicate; we'll bubble them up as API exceptions
                    try {
                        seenProduct = isProductNumberInHistory.test(productNumber);
                    } catch (Exception e) {
                        throw new ApiException("SeenProductPredicate bubbled up an exception", e);
                    }

                    // as above, we want to check if it actually has the appropriate primary_category
                    // if it's a beer that's in-stock and we haven't seen it before, we're done!
                    if (result.getString("primary_category").equalsIgnoreCase("beer")
                            && !seenProduct
                            && isProductAvailable(storeId, productNumber))
                    {
                        return new Beer(
                            productNumber,
                            result.getString("name"),
                            result.optString("tertiary_category"),
                            result.optString("tasting_note"),
                            result.optString("image_url"),
                            result.getInt("price_in_cents"),
                            result.getInt("volume_in_milliliters")
                        );
                    }
                }
            }

            // we exhausted all pages and couldn't find a beer.
            return null;

        } catch (UnirestException e) {
            throw new ApiException(e.getMessage());
        }
    }

}
