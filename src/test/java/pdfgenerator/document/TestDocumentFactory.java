package pdfgenerator.document;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.pdfa.PdfADocument;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PdfADocumentFactory.class})
public class TestDocumentFactory extends EasyMockSupport {
    @Before
    public void initialize() throws Exception {
        PowerMock.mockStatic(PdfADocumentFactory.class);
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
    public void TestDocumentBuild() throws IOException {
        String filename = "abc";
        PdfADocument doc = mock(PdfADocument.class);

        doc.setDefaultPageSize(EasyMock.anyObject(PageSize.class));
        EasyMock.expectLastCall();

        PageSize size = new PageSize(20f,20f);
        EasyMock.expect(doc.getDefaultPageSize()).andReturn(size);
        EasyMock.expect(PdfADocumentFactory.build(filename)).andReturn(doc);

        replayAll();

        Document result = DocumentFactory.build(filename);

        assertThat(result, is(not(nullValue())));
        assertThat(result.getTopMargin(), is(12.7f));
        assertThat(result.getBottomMargin(), is(80.0f));
        assertThat(result.getLeftMargin(), is(36.0f));
        assertThat(result.getRightMargin(), is(36.0f));
    }
}
