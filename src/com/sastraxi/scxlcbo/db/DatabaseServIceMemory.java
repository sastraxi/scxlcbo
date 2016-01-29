package com.sastraxi.scxlcbo.db;

import com.sastraxi.scxlcbo.exception.DatabaseException;
import com.sastraxi.scxlcbo.model.Beer;
import com.sun.scenario.effect.Offset;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by sastr on 2016-01-28.
 */
public class DatabaseServiceMemory implements DatabaseService {

    TreeMap<OffsetDateTime, Beer> history;
    HashSet<Long> seenProducts;

    public DatabaseServiceMemory() {
        history = new TreeMap<>();
        seenProducts = new HashSet<>();
    }

    @Override
    public void addHistory(Beer beer) throws DatabaseException {
        history.put(OffsetDateTime.now(), beer);
        seenProducts.add(beer.getProductNumber());
    }

    @Override
    public boolean isBeerInHistory(long productNumber) throws DatabaseException {
        return seenProducts.contains(productNumber);
    }

    @Override
    public NavigableMap<OffsetDateTime, Beer> getHistory() throws DatabaseException {
        return history.descendingMap();
    }

    @Override
    public void reset() throws DatabaseException {
        history.clear();
        seenProducts.clear();
    }
}
