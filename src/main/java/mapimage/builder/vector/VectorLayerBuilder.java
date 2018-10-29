package mapimage.builder.vector;

import mapimage.utils.VectorLayerUtils;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.map.FeatureLayer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import pdfgenerationapi.models.Feature;
import pdfgenerator.exceptions.ImageGenerationException;

import java.util.List;

/**
 * Abstract class that defines the base structure of a VectorLayerBuilder.
 * Can be extended for different feature types and styles
 */
public abstract class VectorLayerBuilder {

    /**
     * Gets the {@link FeatureLayer} from the specified feature collection
     * @param featureCollection The feature collection
     * @return A feature layer
     * @throws ImageGenerationException If an error occurs when getting the feature source
     */
    public abstract FeatureLayer get(List<Feature> featureCollection) throws ImageGenerationException;
    abstract SimpleFeatureTypeBuilder getFeatureTypeBuilder();

    SimpleFeature getSimpleFeature(Feature feature, SimpleFeatureType type) throws ImageGenerationException {
        SimpleFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder(type);
        simpleFeatureBuilder.add(VectorLayerUtils.getGeometry(feature));

        return simpleFeatureBuilder.buildFeature(null);
    }

    SimpleFeatureSource getFeatureSource(List<Feature> featureCollection) throws ImageGenerationException {
        SimpleFeatureTypeBuilder b = getFeatureTypeBuilder();
        final SimpleFeatureType TYPE = b.buildFeatureType();
        SimpleFeatureCollection collectionLines = new DefaultFeatureCollection("internal",TYPE);

        for (Feature feature : featureCollection) {
            ((DefaultFeatureCollection) collectionLines).add(getSimpleFeature(feature, TYPE));
        }

        return new CollectionFeatureSource(collectionLines);
    }

}
