package mapimage.layers;

import mapimage.utils.VectorLayerUtils;
import mapimage.builder.vector.*;
import org.geotools.map.FeatureLayer;
import pdfgenerationapi.models.Feature;
import pdfgenerationapi.models.Geometry;
import pdfgenerator.exceptions.ImageGenerationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for generating FeatureLayer objects and retrieving Geometries from Feature objects
 */
public class VectorLayerRetriever {

    /**
     * Creates {@link FeatureLayer} collection containing all of the specified features.
     * Each layer will contain features of a specific type.  A layer will only be added if it contains any features.
     * @param featureCollection A collection of {@link Feature} objects to be included in
     * @return A list of {@link FeatureLayer} objects
     * @throws ImageGenerationException If an error occurs when retrieving a {@link Geometry} from a {@link Feature}
     */
    public static List<FeatureLayer> getLayers(List<Feature> featureCollection) throws ImageGenerationException {
        List<FeatureLayer> result = new ArrayList<>();

        VectorLayerBuilder pointLayerBuilder = new PointVectorLayerBuilder();
        VectorLayerBuilder lineLayerBuilder = new LineVectorLayerBuilder();
        VectorLayerBuilder polygonLayerBuilder = new PolygonVectorLayerBuilder();
        VectorLayerBuilder multiPolygonLayerBuilder = new MultiPolygonVectorLayerBuilder();

        addLayerToList(pointLayerBuilder, VectorLayerUtils.filterByGeometryType(featureCollection, "Point"), result);
        addLayerToList(lineLayerBuilder, VectorLayerUtils.filterByGeometryType(featureCollection, "LineString"), result);
        addLayerToList(polygonLayerBuilder, VectorLayerUtils.filterByGeometryType(featureCollection, "Polygon"), result);
        addLayerToList(multiPolygonLayerBuilder, VectorLayerUtils.filterByGeometryType(featureCollection, "MultiPolygon"), result);

        return result;
    }

    private static void addLayerToList(VectorLayerBuilder builder, List<Feature> featureCollection, List<FeatureLayer> featureLayerList) throws ImageGenerationException {
        FeatureLayer layer = builder.get(featureCollection);

        if (layer != null) {
            featureLayerList.add(layer);
        }
    }

}
