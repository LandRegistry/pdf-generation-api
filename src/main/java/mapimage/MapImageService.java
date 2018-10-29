package mapimage;

import mapimage.builder.MapContentBuilder;
import mapimage.builder.MapImageBuilder;
import mapimage.builder.WebMapServerClientBuilder;
import mapimage.layers.WMSLayerRetriever;
import mapimage.layers.VectorLayerRetriever;
import org.geotools.data.wms.WebMapServer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.WMSLayer;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pdfgenerationapi.Config;
import pdfgenerationapi.models.Feature;
import pdfgenerator.exceptions.ImageGenerationException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point to the static image map service
 */
public final class MapImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapImageService.class);

    private static MapImageService instance = null;
    private static WebMapServer baseLayerWMS = null;
    private static WebMapServer geoserverWMS = null;
    private static org.geotools.data.ows.Layer baseLayer = null;
    private static org.geotools.data.ows.Layer nonMigratedLayer = null;
    private static CoordinateReferenceSystem crs = null;

    /**
     * Singleton that returns an instance of MapImageService
     * @return A MapImageService object
     * @throws ImageGenerationException If an error occurs when initialising the MapImageService object
     */
    public static synchronized MapImageService getInstance() throws ImageGenerationException {
        if (instance == null) {
            instance = new MapImageService();
        }
        return instance;
    }

    private MapImageService() throws ImageGenerationException {
        LOGGER.info("Initialising map image service");

        try {
            baseLayerWMS = WebMapServerClientBuilder.build(Config.WMS_SERVER_URL);
            baseLayer = WMSLayerRetriever.get(Config.WMS_LAYER_NAME, baseLayerWMS);
            geoserverWMS = WebMapServerClientBuilder.build(Config.GEOSERVER_URL);
            nonMigratedLayer = WMSLayerRetriever.get(Config.GEOSERVER_BOUNDARY_LAYER_NAME,
                    Config.GEOSERVER_NON_MIGRATED_STYLE_NAME, geoserverWMS);
            crs = CRS.decode("EPSG:27700");
        } catch (IOException | FactoryException | ServiceException | URISyntaxException e) {
            String message = "Error when initialising map image generation service.";
            LOGGER.error(message);
            throw new ImageGenerationException(message, e);
        }
    }

    /**
     * Generates an image of a map with the specified features drawn on it
     * @param featureCollection Features to be drawn on the map
     * @param width Width (in pixels) of the generated image
     * @param height Height (in pixels) of the generated image
     * @return A BufferedImage object containing a map image centered on the specified feature(s)
     * @throws ImageGenerationException If an error occurs when generating the map image
     */
    public BufferedImage generateImage(List<Feature> featureCollection, int width, int height) throws ImageGenerationException {
        if (featureCollection.size() < 1) {
            LOGGER.error("Error generating map image, no features defined in feature collection");
            throw new ImageGenerationException("Error generating map image, no features defined in feature collection");
        }

        BufferedImage mapImage;
        MapContent mapContent = null;
        try {
            LOGGER.info("Generating static map image");
            long startTime = System.nanoTime();

            ArrayList<Layer> layers = new ArrayList<>();

            WMSLayer baseWMSLayer = new WMSLayer(baseLayerWMS, baseLayer);
            WMSLayer nonMigratedWMSLayer = new WMSLayer(geoserverWMS, nonMigratedLayer);
            List<FeatureLayer> featureLayers = VectorLayerRetriever.getLayers(featureCollection);

            layers.add(baseWMSLayer);
            layers.add(nonMigratedWMSLayer);
            layers.addAll(featureLayers);

            ReferencedEnvelope featureLayerBounds = getMinBounds(featureLayers);
            Rectangle imageBounds = new Rectangle(0, 0, width, height);
            ReferencedEnvelope mapBounds = MapImageBuilder.getMapBounds(featureLayerBounds, imageBounds, crs);
            mapContent = MapContentBuilder.build(layers, crs, mapBounds);

            mapImage = MapImageBuilder.build(mapContent, imageBounds, mapBounds);

            long duration = (System.nanoTime() - startTime) / 1000000;
            LOGGER.info("Map image generation ran for '{}' milliseconds.", Long.toString(duration));
        } catch (IOException | FontFormatException e) {
            LOGGER.error("Failed to generate static map image");
            throw new ImageGenerationException("Failed to generate static map image", e);
        } finally {
            if (mapContent != null) {
                mapContent.dispose();
            }
        }

        return mapImage;
    }

    private static ReferencedEnvelope getMinBounds(List<FeatureLayer> featureLayers) {
        ReferencedEnvelope minBounds = new ReferencedEnvelope();

        for (FeatureLayer layer : featureLayers) {
            minBounds.expandToInclude(layer.getBounds());
        }

        // If minimum bounds are equal to maximum bounds - this means only a single point should be shown
        double minX = minBounds.getMinX();
        double minY = minBounds.getMinY();
        double maxX = minBounds.getMaxX();
        double maxY = minBounds.getMaxY();

        if (minX == maxX && minY == maxY) {
            // Zoom out so we can see the point
            minBounds.expandBy(20);
        }

        return minBounds;
    }
}
