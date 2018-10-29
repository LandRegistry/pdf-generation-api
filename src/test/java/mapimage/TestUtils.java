package mapimage;

import com.fasterxml.jackson.databind.ObjectMapper;
import pdfgenerationapi.models.Feature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    public static List<Feature> buildFeatureCollection(String[] featureJson) {
        ArrayList<Feature> result = new ArrayList<>();

        for (String feature : featureJson) {
            Feature feat = buildFeature(feature);
            result.add(feat);
        }

        return result;
    }

    private static Feature buildFeature(String geoJson) {
        ObjectMapper mapper = new ObjectMapper();
        Feature feature = null;
        try {
            feature = mapper.readValue(geoJson, Feature.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return feature;
    }
}
