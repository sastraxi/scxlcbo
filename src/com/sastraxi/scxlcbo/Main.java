package com.sastraxi.scxlcbo;

/**
 * Created by sastr on 2016-01-27.
 */
import static spark.Spark.*;
public class Main {

    public static void main(String[] args) {
        get("/hello", (req, res) -> {
            return "Hello World!";
        });
    }

}
