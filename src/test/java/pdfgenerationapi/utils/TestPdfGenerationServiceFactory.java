package pdfgenerationapi.utils;

import org.junit.Test;
import pdfgenerationapi.services.LLC1.IPdfGenerationService;
import pdfgenerationapi.services.LLC1.llc1PdfGenerationService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestPdfGenerationServiceFactory {

    @Test
    public void Testllc1Instance() {
        IPdfGenerationService service = PdfGenerationServiceFactory.getPdfGenerationInstance("llc1");

        assertThat(service, is(not(nullValue())));
        assertThat(service instanceof llc1PdfGenerationService, is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestUnknownInstance() {
        IPdfGenerationService service = PdfGenerationServiceFactory.getPdfGenerationInstance("abc");
    }

    @Test()
    public void Testinitialize() {
        PdfGenerationServiceFactory pdfGenerationServiceFactory = new PdfGenerationServiceFactory();
        assertThat(pdfGenerationServiceFactory, is(not(nullValue())));
    }
}
