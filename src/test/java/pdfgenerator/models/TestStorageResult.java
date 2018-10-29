package pdfgenerator.models;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestStorageResult {

    @Test
    public void testInitialize() {
        StorageResult result = new StorageResult("document", "external-link");

        assertThat(result.getDocumentUrl(), is("document"));
        assertThat(result.getExternalUrl(), is("external-link"));
    }

    @Test
    public void testSetters() {
        StorageResult result = new StorageResult("document", "external-link");

        assertThat(result.getDocumentUrl(), is("document"));
        assertThat(result.getExternalUrl(), is("external-link"));

        result.setDocumentUrl("new-document");
        result.setExternalUrl("new-external-link");

        assertThat(result.getDocumentUrl(), is("new-document"));
        assertThat(result.getExternalUrl(), is("new-external-link"));
    }
}
