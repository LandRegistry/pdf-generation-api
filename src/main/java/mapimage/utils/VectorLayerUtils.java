package mapimage.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pdfgenerationapi.models.Feature;
import pdfgenerationapi.models.Geometry;
import pdfgenerator.exceptions.ImageGenerationException;

import java.io.IOException;
import java.util.List;

/**
 * Helper class for vector layer functions
 */
public final class VectorLayerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(VectorLayerUtils.class);

    private VectorLayerUtils() {}

    /**
     * Gets the {@link Geometry} object from the specified {@link Feature} object
     * @param feature The feature
     * @return The geometry
     * @throws ImageGenerationException If unable to convert feature geometry JSON to {@link Geometry} object
     */
    public static com.vividsolutions.jts.geom.Geometry getGeometry(Feature feature) throws ImageGenerationException {
        GeometryJSON geoJson = new GeometryJSON();
        Geometry featureGeometry = feature.getGeometry();
        ObjectMapper mapper = new ObjectMapper();

        com.vividsolutions.jts.geom.Geometry shape;
        try {
            String json = mapper.writeValueAsString(featureGeometry);
            shape = geoJson.read(json);
        }
        catch (IOException e) {
            LOGGER.error("Failed to convert vector to json string");
            throw new ImageGenerationException("Failed to convert vector to json string", e);
        }

        return shape;
    }

    /**
     * Adds all {@link Feature} objects to the {@link SimpleFeatureCollection} object using the {@link SimpleFeatureType}
     * @param features A list of feature objects
     * @param type The SimpleFeatureType to be used
     * @return A {@link SimpleFeatureCollection} containing all of the specified Features
     * @throws ImageGenerationException If an error occurs when getting the {@link Geometry} for a Feature
     */
    public static SimpleFeatureCollection getSimpleFeatureCollection(List<Feature> features, SimpleFeatureType type)
        throws ImageGenerationException {
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("internal",type);

        for (Feature feature : features) {
            SimpleFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder(type);
            simpleFeatureBuilder.add(getGeometry(feature));

            featureCollection.add(simpleFeatureBuilder.buildFeature(null));
        }

        return featureCollection;
    }

    /**
     * Takes a list of {@link Feature} objects, and returns a list of {@link Feature} objects with the specified geometry type.
     * @param features A list of {@link Feature} objects
     * @param geometryType The geometry type to be returned. Can be one of 'Point', 'LineString' and 'Polygon'
     * @return The input list filtered to only include the given geometryType.
     */
    public static List<Feature> filterByGeometryType(Iterable<Feature> features, String geometryType) {
        List<Feature> result = Lists.newArrayList();
        for (Feature feature : features) {
            if (feature.getGeometry().getType().equals(geometryType)) {
                result.add(feature);
            }
        }

        return result;
    }

}
