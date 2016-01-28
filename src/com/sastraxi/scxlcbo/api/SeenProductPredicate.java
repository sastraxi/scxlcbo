package com.sastraxi.scxlcbo.api;

/**
 * Created by sastr on 2016-01-28.
 */
@FunctionalInterface
public interface SeenProductPredicate {

    boolean test(int productNumber) throws Exception;

}
