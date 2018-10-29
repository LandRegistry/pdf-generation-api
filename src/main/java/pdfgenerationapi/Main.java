package pdfgenerationapi;

import static spark.Spark.port;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerationapi.views.General;
import pdfgenerationapi.views.v1_0.Llc1controller;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    /**
     * This is the method through which the app runs.
     * It sets the port, registers filters (code that runs before and after each request),
     * and registers the routes and exception handlers.
     */
    public static void main(String[] args) {
        port(Config.PORT);

        new Filters().registerFilters();

        new General().registerRoutes();
        new Llc1controller().registerRoutes();
        LOGGER.info("All routes registered");

        new Exceptions().registerExceptions();
    }
}