package pdfgenerator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerationapi.Config;
import pdfgenerator.exceptions.StorageException;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.databind.ObjectMapper;
import pdfgenerator.models.StorageResult;

/**
 * This service is responsible for calling the storage-api to store generated PDFs into the file store.
 */
public final class StorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    private static final String STORAGE_API_LLC1_BUCKET = "llc1";
    private static final String PDF_CONTENT_TYPE = "application/pdf";

    private StorageService() {}

    /**
     * Saves the given file to the file store.
     * @param file The PDF to save to the file store.
     * @return The URL to the file in the file store.
     * @throws StorageException If saving the file to the file store failed.
     */
    public static StorageResult save(File file) throws StorageException {
        LOGGER.info("Attempting to save generated PDF.");

        StorageResult result;
        try {
            result = call(file);
        }
        catch (StorageException | UnirestException | URISyntaxException e) {
            LOGGER.debug("Failed to save pdf.");
            throw new StorageException("Failed to save pdf.", e);
        }

        LOGGER.info("Successfully saved generated PDF. Filestore URL: {}", result.getDocumentUrl());
        return result;
    }

    /**
     * Calls the storage-apis endpoint for adding files to the filestore.
     * @param file The PDF to save to the file store.
     * @return The URL to the file in the file store.
     * @throws UnirestException If the call to the storage-api failed.
     * @throws StorageException If the status code returned from the call to the search-api was
     *                          {@link HttpStatus#SC_BAD_REQUEST}.
     * @throws URISyntaxException If the URL to the storage-api could not be built.
     */
    private static StorageResult call(File file) throws UnirestException, StorageException, URISyntaxException {
        String storageApiUrl = buildStorageApiUrl().toString();
        HttpResponse<JsonNode> response =
            Unirest
                .post(storageApiUrl)
                .field("file", file, PDF_CONTENT_TYPE)
                .asJson();

        if (response.getStatus() != HttpStatus.SC_CREATED) {
            throw new StorageException("Call to storage API returned a failed status code: " + response.getStatus());
        }

        JSONObject document_info = response.getBody().getObject().getJSONArray("file").getJSONObject(0);
        String bucket = document_info.get("bucket").toString();
        String uuid = document_info.get("file_id").toString();

        StorageResult result = new StorageResult(buildStorageApiResponseUrl(bucket, uuid).toString(),
                document_info.get("external_reference").toString());

        return result;
    }

    /**
     * Builds and returns a URL pointing to the storage-api end point for saving documents.
     * @return URL pointing to the storage-api end point for saving documents.
     * @throws URISyntaxException If the URL could not be constructed.
     */
    private static URI buildStorageApiUrl() throws URISyntaxException {
        String path = Config.STORAGE_API + "/" +STORAGE_API_LLC1_BUCKET;
        URI url = new URIBuilder(path)
            .build();

        LOGGER.debug("Built storage-api URL: {}", url);
        return url;
    }

    /**
     * Builds the URL to the file within the given bucket, with the given fileId.
     * @param bucket The bucket holding the required document.
     * @param fileId The id of the file.
     * @return A URL pointing to the file in the given bucket, with the given fileId in the file store.
     * @throws URISyntaxException If the URL could not be constructed.
     */
    private static URI buildStorageApiResponseUrl(String bucket, String fileId) throws URISyntaxException {
        String path = Config.STORAGE_API + "/" + bucket + "/" + fileId;
        URI url = new URIBuilder(path)
            .build();

        LOGGER.debug("Built URL to save PDF: {}", url);
        return url;
    }
}
