package pdfgenerator.document;

import com.itextpdf.pdfa.PdfADocument;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestPdfADocumentFactory {

    @Test
    public void testPdaBuild() throws IOException {
        File testFile = new File("abc.pdf");
        PdfADocument doc = PdfADocumentFactory.build(testFile.getPath());
        assertThat(doc, is(not(nullValue())));
        assertThat(doc.getDocumentInfo().getTitle(), is("Local Land Charges Official Search Certificate"));
        if(testFile.exists()) {
            FileUtils.deleteQuietly(testFile);
        }

    }
}
