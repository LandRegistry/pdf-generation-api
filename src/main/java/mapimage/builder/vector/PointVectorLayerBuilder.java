package mapimage.builder.vector;

import com.vividsolutions.jts.geom.Point;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.map.FeatureLayer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import pdfgenerationapi.models.Feature;
import pdfgenerator.exceptions.ImageGenerationException;

import java.awt.*;
import java.util.List;

/**
 * Implementation of {@link VectorLayerBuilder} which creates a FeatureLayer containing points
 */
public class PointVectorLayerBuilder extends VectorLayerBuilder {

    private static final float OPACITY = 1;
    private static final float LINE_SIZE = 5;
    /**
     * Gets the {@link FeatureLayer} from the specified feature collection
     * @param featureCollection The feature collection
     * @return A feature layer
     * @throws ImageGenerationException If an error occurs when getting the feature source
     */
    public FeatureLayer get(List<Feature> featureCollection) throws ImageGenerationException {
        if (featureCollection.size() < 1) {
            return null;
        }

        SimpleFeatureSource source = getFeatureSource(featureCollection);
        Style style = SLD.createPointStyle("Circle", Color.BLUE, Color.WHITE, OPACITY, LINE_SIZE);

        return new FeatureLayer(source, style);
    }

    SimpleFeatureTypeBuilder getFeatureTypeBuilder() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();

        builder.setName("PointFeature");
        builder.add("point", Point.class);

        return builder;
    }

}
