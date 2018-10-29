package pdfgenerationapi.services.llc1;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.models.Llc1GenerationResult;
import pdfgenerationapi.models.Llc1PdfRequest;
import pdfgenerationapi.services.LLC1.llc1PdfGenerationService;
import pdfgenerator.core.LLC1;
import pdfgenerator.core.LLC1GenerationFactory;
import pdfgenerator.exceptions.PdfGenerationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNot.not;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LLC1GenerationFactory.class})
public class Testllc1PdfGenerationService extends EasyMockSupport {

    @Before
    public void initialize() throws Exception {
        PowerMock.mockStatic(LLC1GenerationFactory.class);
    }

    @After
    public void after() {
        super.resetAll();
        PowerMock.resetAll();
    }

    @Override
    public void verifyAll() {
        super.verifyAll();
        PowerMock.verifyAll();
    }

    @Override
    public void replayAll() {
        super.replayAll();
        PowerMock.replayAll();
    }

    @Test
    public void testGeneratePDF() throws PdfGenerationException {
        Llc1PdfRequest request = mock(Llc1PdfRequest.class);
        LLC1 llc1 = mock(LLC1.class);
        Llc1GenerationResult mockResult = mock(Llc1GenerationResult.class);

        EasyMock.expect(mockResult.getDocumentUrl()).andReturn("url-to-document");
        EasyMock.expect(LLC1GenerationFactory.getLlc1PdfGenerator()).andReturn(llc1);
        EasyMock.expect(llc1.generate(request)).andReturn(mockResult);

        replayAll();

        llc1PdfGenerationService service = new llc1PdfGenerationService();
        Llc1GenerationResult result = service.generatePDF(request);

        assertThat(result, is(not(nullValue())));
        assertThat(result.getDocumentUrl(), is("url-to-document"));
    }
}
