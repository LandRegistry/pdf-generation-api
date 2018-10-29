package mapimage.layers;

import org.geotools.data.ows.Layer;
import org.geotools.data.ows.StyleImpl;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WMSUtils;
import org.geotools.data.wms.WebMapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pdfgenerator.exceptions.ImageGenerationException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class responsible for constructing the base layer of the map
 */
public class WMSLayerRetriever {
    private static final Logger LOGGER = LoggerFactory.getLogger(WMSLayerRetriever.class);

    private WMSLayerRetriever() { }

    /**
     * Retrieves the WMS layer with the specified {@code layerName} using the getCapabilities function.
     * This will use the default styling as defined in the WMS server config.
     * Throws an ImageGenerationException if the layer is not found
     * @param layerName The name of the layer to be returned
     * @param wms The WMS server object
     * @return The WMS layer object
     * @throws ImageGenerationException If the layer could not be found
     */
    public static Layer get(String layerName, WebMapServer wms) throws ImageGenerationException {
        return getLayer(layerName, wms);
    }

    /**
     * Retrieves the WMS layer with the specified {@code layerName} and {@code styleName} using
     * the getCapabilities function.
     * Throws an ImageGenerationException if the layer is not found
     * @param layerName The name of the layer to be returned
     * @param styleName The name of the style to be applied to the layer
     * @param wms The WMS server object
     * @return The WMS layer object
     * @throws ImageGenerationException If the layer or style could not be found
     */
    public static Layer get(String layerName, String styleName, WebMapServer wms) throws ImageGenerationException {
        Layer layer = getLayer(layerName, wms);
        StyleImpl style = WMSLayerRetriever.getStyle(layer, styleName);

        List<StyleImpl> styles = new ArrayList<>();
        styles.add(style);
        layer.setStyles(styles);

        return layer;
    }

    private static Layer getLayer(String layerName, WebMapServer wms) throws ImageGenerationException {
        LOGGER.info("Retrieving base layer from WMS server.");
        LOGGER.debug("Layer name: '{}'", layerName);
        WMSCapabilities capabilities = wms.getCapabilities();

        Layer[] layers = WMSUtils.getNamedLayers(capabilities);

        for (Layer layer : layers) {
            if(layer.getName().equals(layerName)) {
                LOGGER.info("Layer found.");
                return layer;
            }
        }

        String message = "Unable to find layer '" + layerName + "' in WMS service.";
        LOGGER.error(message);
        throw new ImageGenerationException(message);
    }

    private static StyleImpl getStyle(Layer layer, String styleName) throws ImageGenerationException {
        List styles = layer.getStyles();
        for (Iterator it = styles.iterator(); it.hasNext();) {
            StyleImpl elem = (StyleImpl) it.next();

            if (elem.getName().equals(styleName)) {
                return elem;
            }
        }

        String message = "Unable to find style '" + styleName + "' in layer";
        LOGGER.error(message);
        throw new ImageGenerationException(message);
    }
}
