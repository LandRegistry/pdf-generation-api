package pdfgenerator.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

public class TestDocumentItem {

    @Test
    public void testInitialize() {
        DocumentItem document = new DocumentItem();

        document.setBucket("bucket");
        document.setReference("reference");
        document.setFileId("id");
        document.setSubdirectory("some/subdirectory");

        assertThat(document.getBucket(), is("bucket"));
        assertThat(document.getReference(), is("reference"));
        assertThat(document.getFileId(), is("id"));
        assertThat(document.getSubdirectory(), is("some/subdirectory"));
    }

    @Test
    public void testInitializeFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{ \"bucket\": \"bucket\", \"reference\": \"reference\", \"file_id\": \"id\", \"subdirectory\": \"some/subdirectory\"}";
        DocumentItem document = mapper.readValue(json, DocumentItem.class);

        assertThat(document.getBucket(), is("bucket"));
        assertThat(document.getReference(), is("reference"));
        assertThat(document.getFileId(), is("id"));
        assertThat(document.getSubdirectory(), is("some/subdirectory"));
    }

    @Test
    public void testToString() {
        DocumentItem document = new DocumentItem();

        document.setBucket("bucket");
        document.setReference("reference");
        document.setFileId("id");
        document.setSubdirectory("some/subdirectory");

        String result = document.toString();

        assertThat(result, containsStringIgnoringCase("bucket=bucket"));
        assertThat(result, containsStringIgnoringCase("reference=reference"));
        assertThat(result, containsStringIgnoringCase("fileId=id"));
        assertThat(result, containsStringIgnoringCase("subdirectory=some/subdirectory"));

    }

}
