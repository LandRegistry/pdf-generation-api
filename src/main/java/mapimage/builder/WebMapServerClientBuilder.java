package mapimage.builder;

import org.apache.http.client.utils.URIBuilder;
import org.geotools.data.wms.WebMapServer;
import org.geotools.ows.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class responsible for building WebMapServer objects
 */
public class WebMapServerClientBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebMapServerClientBuilder.class);

    private WebMapServerClientBuilder() {}

    /**
     * Builds a WebMapServer object using the specified URL
     * @param url The URL to the WMS
     * @return A WebMapServer object configured for the specified URL
     * @throws ServiceException Service exception
     * @throws IOException I/O exception
     */
    public static WebMapServer build(String url) throws ServiceException, IOException, URISyntaxException {
        LOGGER.debug("Building WMS server object");

        URIBuilder uriBuilder = new URIBuilder(url);
        URI uri = uriBuilder.build();

        return new WebMapServer(uri.toURL());
    }

}
