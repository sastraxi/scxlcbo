package com.sastraxi.scxlcbo;

/**
 * Created by sastr on 2016-01-27.
 */

import com.sastraxi.scxlcbo.api.LcboApi;
import com.sastraxi.scxlcbo.db.DatabaseService;
import com.sastraxi.scxlcbo.db.DatabaseServiceMemory;
import com.sastraxi.scxlcbo.db.DatabaseServiceRethink;
import com.sastraxi.scxlcbo.exception.ApiException;
import com.sastraxi.scxlcbo.model.Beer;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static spark.Spark.get;
public class Main {

    public static final String GREETING_HTML = "Hi Michelle!";

    public static final String[] WELCOME_HTML = {
        "... is it Friday already?",
        "Let me take you on a beer discovery journey.",
        "Did you know that the LCBO was established in 1927?",
        "Great beer is just a click away.",
        "Canada drank 2.3 billion litres of beer in 2012!"
    };

    public static final String[] TRY_AGAIN_HTML = {
        "Not feeling it?",
        "This beer too \"2015\" for you?",
        "I've got a good feeling about this next one...",
        "Let's try that again...",
        "What's that? You'd rather drink water?",
        "Wrong side of the hop spectrum?",
        "Not trappist enough for you?",
    };

    // LCBO api key registered to sastraxi@gmail.com
    public static final String LCBO_API_KEY = "MDo4ODNjMzgyMC1jNTZkLTExZTUtYjFlZi1hYjlkMWUwZGQzNGY6QTV3S0xxcm5OV2xWb3kzRGhvUzJQM1QyQXdCQjRGYXVMUTZ5";
    public static final int LCBO_STORE_ID = 511;

    // If you'd like to use RethinkDB in order to store historical beer entries between runs,
    // use the appropriate database service and make sure to call reset() on it before using!
    // N.B. the CI server will replace this hostname with localhost!
    public static final String DB_HOSTNAME = "ec2-54-172-103-174.compute-1.amazonaws.com";
    public static final int DB_PORT = 28015;

    private static Random random = new Random();

    public static void main(String[] args) {
        // set up template engine
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine();
        Configuration freeMarkerConfiguration = new Configuration();
        freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(Main.class, "/"));
        freeMarkerEngine.setConfiguration(freeMarkerConfiguration);

        // our back-end
        LcboApi lcbo = new LcboApi(LCBO_API_KEY);
        DatabaseService db = new DatabaseServiceRethink(DB_HOSTNAME, DB_PORT);
        //DatabaseService db = new DatabaseServiceMemory(); // doesn't require a database

        // Hi, Michelle! It's beer time! etc.
        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("history", db.getHistory());
            attributes.put("greeting", GREETING_HTML);
            attributes.put("message", WELCOME_HTML[random.nextInt(WELCOME_HTML.length)]);
            return freeMarkerEngine.render(new ModelAndView(attributes, "welcome.ftl"));
        });

        // Generate and show our random beer.
        get("/suggest", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            try {
                Beer randomBeer = lcbo.getNewRandomAvailableBeer(LCBO_STORE_ID, productNumber -> db.isBeerInHistory(productNumber));
                if (randomBeer != null) { // if it's null, then no new beers are in stock! template handles it
                    db.addHistory(randomBeer);
                    attributes.put("beer", randomBeer);
                    attributes.put("message", TRY_AGAIN_HTML[random.nextInt(TRY_AGAIN_HTML.length)]);
                }
                attributes.put("history", db.getHistory().entrySet());
                return freeMarkerEngine.render(new ModelAndView(attributes, "suggestion.ftl"));

            } catch (ApiException e) {

                // show an error view if the API (or database through our predicate) behaved poorly.
                attributes.put("exception", e);
                return freeMarkerEngine.render(new ModelAndView(attributes, "error.ftl"));
            }
        });

        // Tear down/set up our database.
        get("/reset/yes_im_sure/everything_will_be_gone", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            db.reset();
            return freeMarkerEngine.render(new ModelAndView(attributes, "resetComplete.ftl"));
        });
    }

}
