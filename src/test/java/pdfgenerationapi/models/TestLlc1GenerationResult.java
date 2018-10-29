package pdfgenerationapi.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import pdfgenerator.models.LocalLandCharge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

public class TestLlc1GenerationResult {

    @Test
    public void testInitialize() {
        List<LocalLandCharge> includedCharges = new ArrayList<>();
        Llc1GenerationResult llc1GenerationResult = new Llc1GenerationResult("abc", "external-link", includedCharges);
        assertThat(llc1GenerationResult.getDocumentUrl(), is("abc"));
        assertThat(llc1GenerationResult.getIncludedCharges(), is(includedCharges));
    }

    @Test
    public void testExportToJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<LocalLandCharge> includedCharges = new ArrayList<>();
        Llc1GenerationResult llc1GenerationResult = new Llc1GenerationResult("abc", "external-link", includedCharges);

        String json = mapper.writeValueAsString(llc1GenerationResult);

        assertThat(llc1GenerationResult.getDocumentUrl(), is(not(nullValue())));
        assertThat(json, is("{\"included_charges\":[],\"document_url\":\"abc\",\"external_url\":\"external-link\"}"));
    }

    @Test
    public void testExportToJsonIncludingCharges() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<LocalLandCharge> includedCharges = new ArrayList<>();
    }

    @Test
    public void testToString() {
        List<LocalLandCharge> includedCharges = new ArrayList<>();
        Llc1GenerationResult llc1GenerationResult = new Llc1GenerationResult("abc", "external-link", includedCharges);
        llc1GenerationResult.setDocumentUrl("123");
        String result = llc1GenerationResult.toString();

        assertThat(result, containsStringIgnoringCase("documentUrl=123"));
    }
}
