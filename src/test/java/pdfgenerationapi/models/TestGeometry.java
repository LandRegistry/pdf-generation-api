package pdfgenerationapi.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestGeometry {

    @Test
    public void testInitialize() {
        Geometry geometry = new Geometry();
        geometry.setType("Polygon");
        geometry.setCoordinates(new ArrayList<>());

        assertThat(geometry.getType(), is("Polygon"));
        assertThat(geometry.getCoordinates().size(), is(0));
    }

    @Test
    public void testInitializeFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"type\": \"Polygon\",\"coordinates\": [[[111.111,111.111],[111.111,111.111],[111.111,111.111]]]}";
        Geometry geometry = mapper.readValue(json,Geometry.class);

        assertThat(geometry.getType(), is("Polygon"));
        assertThat(geometry.getCoordinates().size(), is(1));
    }
}
