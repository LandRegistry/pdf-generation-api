package pdfgenerationapi.models;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TestExtents {

    @Test
    public void testInitialize() {
        Extents extents = new Extents();
        extents.setType("abc");
        extents.setFeatures(new ArrayList<>());

        assertThat(extents.getType(), is("abc"));
        assertThat(extents.getFeatures().size(), is(0));
    }

    @Test
    public void testInitializeFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{ \"type\": \"FeatureCollection\",\"features\": [{\"type\": \"Feature\",\"geometry\": {\"type\": \"Polygon\",\"coordinates\": [[[111.111,111.111],[111.111,111.111],[111.111,111.111]]]},\"properties\": null}]}";
        Extents extents = mapper.readValue(json,Extents.class);

        assertThat(extents.getType(), is("FeatureCollection"));
        assertThat(extents.getFeatures().size(), is(1));
    }
}
