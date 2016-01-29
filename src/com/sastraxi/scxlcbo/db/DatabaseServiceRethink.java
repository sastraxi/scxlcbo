package com.sastraxi.scxlcbo.db;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import com.sastraxi.scxlcbo.exception.DatabaseException;
import com.sastraxi.scxlcbo.model.Beer;
import com.sun.scenario.effect.Offset;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.TimeoutException;

/**
 * Exposes storage functionality provided by a RethinkDB instance.
 */
public class DatabaseServiceRethink implements DatabaseService {

    private static final String DATABASE = "scxlcbo";
    private static final String TABLE_HISTORY = "beer_history";
    private static final String FIELD_PRODUCT_NUMBER = "productNumber";
    private static final String FIELD_TIMESTAMP = "tstamp";

    public static final RethinkDB r = RethinkDB.r;

    private final String hostname;
    private final int port;
    private ThreadLocal<Connection> pool;

    public DatabaseServiceRethink(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.pool = new ThreadLocal<>();
    }

    private Connection getConnection() throws TimeoutException {
        Connection connection = pool.get();
        if (connection == null) {
            connection = r.connection()
                    .hostname(hostname)
                    .port(port)
                    .connect();
            pool.set(connection);
        }
        return connection;
    }

    /**
     * Record a beer suggestion that has just been made.
     *
     * @param beer the beer that has just been suggested.
     * @throws DatabaseException upon any failure to communicate properly with the database.
     */
    @Override
    public void addHistory(Beer beer) throws DatabaseException {
        try {
            Connection conn = getConnection();

            // we'll store all dates in UTC for sanity
            OffsetDateTime utcTimestamp = OffsetDateTime.now(ZoneOffset.UTC);

            r.db(DATABASE).table(TABLE_HISTORY).insert(
                r.hashMap(FIELD_PRODUCT_NUMBER, beer.getProductNumber())
                 .with(FIELD_TIMESTAMP, utcTimestamp)
                 .with("name", beer.getName())
                 .with("style", beer.getStyle())
                 .with("cadCents", beer.getCadCents())
                 .with("mL", beer.getmL()))
            .run(conn);

        } catch (TimeoutException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Queries whether or not a beer has been suggested before.
     *
     * @param productNumber the product number of the beer to search for
     * @return true iff the given beer has been suggested before
     * @throws DatabaseException upon any failure to communicate properly with the database.
     */
    @Override
    public boolean isBeerInHistory(long productNumber) throws DatabaseException {
        try {
            Connection conn = getConnection();

            boolean exists = !((Boolean) r.db(DATABASE).table(TABLE_HISTORY).getAll(productNumber).optArg("index", FIELD_PRODUCT_NUMBER).isEmpty().run(conn));
            return exists;

        } catch (TimeoutException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Grab the history of suggested beers.
     *
     * @return a sorted (newest to oldest) map of timestamps (UTC; when the beer suggestion was made) to suggested beer
     * @throws DatabaseException upon any failure to communicate properly with the database.
     */
    @Override
    public NavigableMap<OffsetDateTime, Beer> getHistory() throws DatabaseException {
        try {
            Connection conn = getConnection();

            // all values from the history table in descending orer of the timestamp index
            ArrayList<HashMap> cursor = r.db(DATABASE).table(TABLE_HISTORY).orderBy().optArg("index", r.asc(FIELD_TIMESTAMP)).orderBy(FIELD_TIMESTAMP).run(conn, HashMap.class);
            TreeMap<OffsetDateTime, Beer> results = new TreeMap<>();

            // loop through the suggestions in the database.
            for (int i = 0; i < cursor.size(); ++i) {
                Object result = cursor.get(i);

                JSONObject next = new JSONObject((HashMap) result);
                results.put(OffsetDateTime.parse(next.getString(FIELD_TIMESTAMP), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    new Beer(
                        next.getLong(FIELD_PRODUCT_NUMBER),
                        next.getString("name"),
                        next.getString("style"),
                        null,
                        null,
                        next.getLong("cadCents"),
                        next.getLong("mL")
                    ));
            }

            return results.descendingMap();

        } catch (TimeoutException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Destroy and re-build the database.
     */
    @Override
    public void reset() throws DatabaseException {
        try {
            Connection conn = getConnection();

            if (r.dbList().contains(DATABASE).run(conn)) {
                r.dbDrop(DATABASE).run(conn);
            }

            r.dbCreate(DATABASE).run(conn);
            r.db(DATABASE).tableCreate(TABLE_HISTORY).optArg("primary_key", FIELD_PRODUCT_NUMBER).run(conn);

            // we'll be ordering by timestamp so might as well index
            // product number is indexed for free (above) as it's the primary key
            r.db(DATABASE).table(TABLE_HISTORY).indexCreate(FIELD_TIMESTAMP).run(conn);

        } catch (TimeoutException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

}
