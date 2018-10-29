package pdfgenerator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerationapi.Config;
import pdfgenerationapi.models.Feature;
import pdfgenerationapi.models.Llc1PdfRequest;

import pdfgenerator.exceptions.SearchException;
import pdfgenerator.models.GeometryCollection;
import pdfgenerator.models.LocalLandCharge;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This service is responsible for calling the search-api to retrieve all Local Land Charges within the extent defined
 * in the input.
 */
public final class SearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private SearchService() {}

    /**
     * Returns all Local Land Charges within the given extent.
     * @param llc1PdfRequest The request passed to the llc1 endpoint containing an extent to return all Local Land
     *                       Charges for.
     * @return A list of {@link LocalLandCharge} objects representing the Local Land Charges in the given area.
     * @throws SearchException If the call to the search-api failed.
     */
    public static List<LocalLandCharge> get(Llc1PdfRequest llc1PdfRequest) throws SearchException {
        LOGGER.info("Retrieving local land charges.");

        List<Feature> features = llc1PdfRequest.getExtents().getFeatures();
        List<LocalLandCharge> localLandCharges = new ArrayList<>(1);
        GeometryCollection geoCollection = new GeometryCollection();
        try {
            for (Feature feature : features) {
            	geoCollection.getGeometries().add(feature.getGeometry());
            }
            String body = call(geoCollection);
            if (body != null) {
                LocalLandCharge[] localLandChargeArray = MAPPER.readValue(body, LocalLandCharge[].class);
                localLandCharges.addAll(Arrays.asList(localLandChargeArray));
            }
        }
        catch (UnirestException | URISyntaxException | IOException e) {
            LOGGER.debug("Failed to call search API.");
            throw new SearchException("Failed to call search API.", e);
        }

        LOGGER.debug("Returning local land charges: {}", localLandCharges);
        return localLandCharges;
    }

    /**
     * Calls the search-api local-land-charges end point passing the bounding box specified in the given geometry and
     * returning the response body.
     * @param geometry The geometry to pass to the local-land-charges end point.
     * @return The body of the response, or null if no results where found.
     * @throws JsonProcessingException If the given geometry could not be deserialized.
     * @throws UnirestException If the call to the search-api failed.
     * @throws SearchException If the status code returned from the call to the search-api was
     *                         {@link HttpStatus#SC_UNPROCESSABLE_ENTITY}.
     * @throws URISyntaxException If the URL to the search-api could not be built.
     */
    private static String call(GeometryCollection geoCollection) throws JsonProcessingException, UnirestException,
        SearchException, URISyntaxException {
        String searchApiUrl = buildSearchApiUrl().toString();
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse<JsonNode> response =
            Unirest
                .post(searchApiUrl)
                .header("Content-Type", "application/json")
                .queryString("filter", "cancelled")
                .body(mapper.writeValueAsString(geoCollection))
                .asJson();

        if (response.getStatus() == HttpStatus.SC_NOT_FOUND) {
            return null;
        }

        if (response.getStatus() != HttpStatus.SC_OK) {
            throw new SearchException("Call to search API returned a failed status code: " + response.getStatus());
        }

        return response.getBody().toString();
    }

    /**
     * Builds and returns a URL pointing to the search-api end point for retrieving local land charges.
     * @return URL pointing to the search-api end point for retrieving local land charges.
     * @throws URISyntaxException If the URL could not be constructed.
     */
    private static URI buildSearchApiUrl() throws URISyntaxException {
        URI url = new URIBuilder(Config.SEARCH_API)
            .build();

        LOGGER.debug("Built search-api URL: {}", url);
        return url;
    }
}
