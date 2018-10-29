package pdfgenerationapi.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

public class TestLlc1PdfRequest {

    @Test
    public void testInitialize(){
        Llc1PdfRequest llc1PdfRequest = new Llc1PdfRequest();
        llc1PdfRequest.setDescription("description");
        llc1PdfRequest.setExtents(new Extents());

        assertThat(llc1PdfRequest.getDescription(), is("description"));
        assertThat(llc1PdfRequest.getExtents(), is(not(nullValue())));
    }

    @Test
    public void testInitializeFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"description\":\"example description\",\"extents\":{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[111.111,111.111],[111.111,111.111],[111.111,111.111]]]},\"properties\":null}]}}";
        Llc1PdfRequest llc1PdfRequest = mapper.readValue(json,Llc1PdfRequest.class);

        assertThat(llc1PdfRequest.getDescription(), is("example description"));
        assertThat(llc1PdfRequest.getExtents(), is(not(nullValue())));
        assertThat(llc1PdfRequest.getExtents().getType(), is("FeatureCollection"));
        assertThat(llc1PdfRequest.getExtents().getFeatures().size(), is(1));
        assertThat(llc1PdfRequest.getExtents().getFeatures().get(0).getType(), is("Feature"));
        assertThat(llc1PdfRequest.getExtents().getFeatures().get(0).getGeometry(), is(not(nullValue())));
        assertThat(llc1PdfRequest.getExtents().getFeatures().get(0).getGeometry().getType(), is("Polygon"));
        assertThat(llc1PdfRequest.getExtents().getFeatures().get(0).getGeometry().getCoordinates().size(), is(1));
    }

    @Test
    public void testToString() {
        Llc1PdfRequest llc1PdfRequest = new Llc1PdfRequest();
        llc1PdfRequest.setDescription("description");
        llc1PdfRequest.setExtents(new Extents());
        String result = llc1PdfRequest.toString();

        assertThat(result, containsStringIgnoringCase("description=description"));
        assertThat(result, containsStringIgnoringCase("extents=pdfgenerationapi.models.Extents"));
    }
}
