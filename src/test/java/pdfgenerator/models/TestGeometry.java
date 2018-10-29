package pdfgenerator.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import pdfgenerationapi.models.Feature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

public class TestGeometry {

    @Test
    public void testInitialize() {
        Feature feature = new Feature();
        List<Feature> features = new ArrayList<>();
        features.add(feature);

        Geometry geometry = new Geometry();
        geometry.setType("abc");
        geometry.setFeatures(features);

        assertThat(geometry.getType(), is("abc"));
        assertThat(geometry.getFeatures().get(0), is(feature));
    }

    @Test
    public void testInitializeFromJsonEmptyFeature() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{ \"type\": \"abc\",\"features\": []}";
        Geometry geometry = mapper.readValue(json,Geometry.class);

        assertThat(geometry.getType(), is("abc"));
        assertThat(geometry.getFeatures().size(), is(0));
    }

    @Test
    public void testInitializeFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{ \"type\": \"abc\",\"features\": [{ \"type\": \"Feature\",\"geometry\": {\"type\": \"Polygon\",\"coordinates\": [[[111.111,111.111],[111.111,111.111],[111.111,111.111]]]},\"properties\": null}]}";
        Geometry geometry = mapper.readValue(json,Geometry.class);

        assertThat(geometry.getType(), is("abc"));
        assertThat(geometry.getFeatures().size(), is(1));
        assertThat(geometry.getFeatures().get(0).getType(), is("Feature"));
        assertThat(geometry.getFeatures().get(0).getProperties(), is(nullValue()));
    }

    @Test
    public void testToString() {
        Feature feature = new Feature();
        List<Feature> features = new ArrayList<>();
        features.add(feature);

        Geometry geometry = new Geometry();
        geometry.setType("abc");
        geometry.setFeatures(features);

        String result = geometry.toString();

        assertThat(result, containsStringIgnoringCase("type=abc"));
        assertThat(result, containsStringIgnoringCase("features=["));
    }
}