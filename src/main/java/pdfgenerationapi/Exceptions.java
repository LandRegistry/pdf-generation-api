package pdfgenerationapi;

import static spark.Spark.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonParseException;

import pdfgenerationapi.utils.JsonTransformer;

class Exceptions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Exceptions.class);

    void registerExceptions() {
        // Application-raised errors
        exception(ApplicationException.class, (exception, request, response) -> {
            // If the app is throwing a 500 itself then it's log-worthy
            if (exception.httpCode == 500) {
            	LOGGER.error("Application Exception: {}", exception.getMessage(), exception);
        	} else {
        		LOGGER.debug("Application Exception: {}", exception.getMessage(), exception);
        	}
            response.type("application/json");
            response.status(exception.httpCode);
            response.body(buildExceptionBody(exception.getMessage(), exception.errorCode));
        });
        
        // If the incoming JSON could not be parsed (e.g. mismatched type in JSON vs target object
        // let's send back a 400 with explanation.
        exception(JsonParseException.class, (exception, request, response) -> {
        	LOGGER.debug("JSON Parse Exception: {}", exception.getMessage(), exception);
            response.type("application/json");
            response.status(400);
            response.body(buildExceptionBody("Invalid input, caused by: " + exception.getMessage(), "XXX"));
        });

        // Emergency unexpected errors that have not been handled in the blocks above (null pointer etc)
        exception(Exception.class, (exception, request, response) -> {
            LOGGER.error("Unhandled Exception: {}", exception.getClass().getName(), exception);
            response.type("application/json");
            response.status(500);
            response.body(buildExceptionBody("Unexpected error.", "XXX"));
        });
        
        LOGGER.info("Exceptions registered");
    }
    
    private String buildExceptionBody(String errorMessage, String errorCode) {
    	// Just use a map as the json source, object is overkill for two fields
    	Map<String, String> result = new HashMap<>(2);
    	result.put("error_message", errorMessage);
    	result.put("error_code", errorCode);
    	return JsonTransformer.render(result);
    }
}