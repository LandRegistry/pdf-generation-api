package pdfgenerationapi.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class Testfeature {

    @Test
    public void testInitialize() {
        Feature feature = new Feature();
        feature.setType("Feature");
        feature.setProperties("property");
        feature.setGeometry(new Geometry());

        assertThat(feature.getType(), is("Feature"));
        assertThat(feature.getProperties(), is("property"));
        assertThat(feature.getGeometry(), is(not(nullValue())));
    }

    @Test
    public void testInitializeFromJson() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String json = "{ \"type\": \"Feature\",\"geometry\": {\"type\": \"Polygon\",\"coordinates\": [[[111.111,111.111],[111.111,111.111],[111.111,111.111]]]},\"properties\": null}}";
        Feature feature = mapper.readValue(json,Feature.class);

        assertThat(feature.getType(), is("Feature"));
        assertThat(feature.getGeometry(), is(not(nullValue())));
        assertThat(feature.getProperties(), is(nullValue()));
        assertThat(feature.getGeometry().getType(), is("Polygon"));
    }
}
