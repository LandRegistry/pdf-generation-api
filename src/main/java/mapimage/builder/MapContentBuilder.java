package mapimage.builder;

import org.geotools.map.Layer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.List;

/**
 * Class responsible for creating a MapContent object with all specified layers in the order they are defined in the array
 */
public final class MapContentBuilder {
    private MapContentBuilder() { }

    /**
     * Build a {@link MapContent} object containing all defined layers with the specified viewport and CRS
     * @param layers A list of layers to be included in the map
     * @param crs The coordinate reference system used in the map
     * @param mapBounds The map bounds
     * @return A {@link MapContent} object
     */
    public static MapContent build(List<Layer> layers, CoordinateReferenceSystem crs, ReferencedEnvelope mapBounds) {
        MapContent content = new MapContent();

        for (Layer layer : layers) {
            content.addLayer(layer);
        }

        MapViewport viewport = new MapViewport();
        viewport.setCoordinateReferenceSystem(crs);
        viewport.setBounds(mapBounds);
        viewport.setMatchingAspectRatio(true);

        content.setViewport(viewport);

        return content;
    }

}
