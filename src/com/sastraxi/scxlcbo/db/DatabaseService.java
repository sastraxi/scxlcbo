package com.sastraxi.scxlcbo.db;

import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import com.sastraxi.scxlcbo.exception.DatabaseException;
import com.sastraxi.scxlcbo.model.Beer;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.NavigableMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by sastr on 2016-01-28.
 */
public interface DatabaseService {

    /**
     * Record a beer suggestion that has just been made.
     *
     * @param beer the beer that has just been suggested.
     * @throws DatabaseException upon any failure to communicate properly with the database.
     */
    public void addHistory(Beer beer) throws DatabaseException;

    /**
     * Queries whether or not a beer has been suggested before.
     *
     * @param productNumber the product number of the beer to search for
     * @return true iff the given beer has been suggested before
     * @throws DatabaseException upon any failure to communicate properly with the database.
     */
    public boolean isBeerInHistory(int productNumber) throws DatabaseException;

    /**
     * Grab the history of suggested beers.
     *
     * @return a sorted (newest to oldest) map of timestamps (UTC; when the beer suggestion was made) to suggested beer
     * @throws DatabaseException upon any failure to communicate properly with the database.
     */
    public NavigableMap<ZonedDateTime, Beer> getHistory() throws DatabaseException;


    /**
     * Destroy and re-build the database.
     */
    public void reset() throws DatabaseException;

}
