package mapimage.builder.vector;

import com.vividsolutions.jts.geom.LineString;
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
 * Implementation of {@link VectorLayerBuilder} which creates a FeatureLayer containing lines
 */
public class LineVectorLayerBuilder extends VectorLayerBuilder {

    private static final float LINE_WIDTH = 1;
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
        Style style = SLD.createLineStyle(Color.BLUE, LINE_WIDTH);

        return new FeatureLayer(source, style);
    }

    SimpleFeatureTypeBuilder getFeatureTypeBuilder() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();

        builder.setName("LineStringFeature");
        builder.add("linestring", LineString.class);

        return builder;
    }

}
