package pdfgenerationapi.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerationapi.ApplicationException;
import pdfgenerationapi.Config;

/**
 * This service is responsible for authenticating JWTs.
 */
public final class AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private AuthenticationService() {}

    /**
     * Authenticates the given JWT with authentication-api and returns boolean of validity.
     * TODO?: Construct object from authentication-api response if permissions are to be used
     * @param jwt A String from the request header with the JWT to validate.
     * @return boolean of whether the JWT is valid.
     * @throws ApplicationException if authentication fails
     */
    public static boolean validate(String jwt) throws ApplicationException {
    	LOGGER.info("Authenticating JWT");
    	if (jwt == null || !jwt.startsWith("Bearer ")) {
            throw new ApplicationException("Invalid JWT header, expected 'Bearer <token>', received '" + jwt + "'",
            		"LLC1-AUTH1", 401);
    	}
    	try {
	        HttpResponse<JsonNode> response =
	            Unirest
	                .post(Config.AUTHENTICATION_API)
	                .field("token", jwt.replaceFirst("Bearer ", ""))
	                .asJson();
	
	        if (response.getStatus() != HttpStatus.SC_OK) {
	            LOGGER.warn("Authentication API returned unexpect response, status code: " + response.getStatus());
	            throw new ApplicationException("Call to authentication API returned a failed status code: " + response.getStatus(),
	            		"LLC1-AUTH2", 401);
	        }
    	}
    	catch (UnirestException e) {
            LOGGER.error("Failed to call authentication API. Exception: {}", e.getMessage(), e);
            throw new ApplicationException("Failed to call authentication API. Exception: " + e.getMessage(), "LLC1-AUTH3", 500);
        }
    	LOGGER.info("JWT Authenticated");
        return true;
    }

}