package pdfgenerator.security;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.signatures.PdfSigner;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

import java.io.File;
import java.io.IOException;

public class TestDigitalSignatureFactory {

    @Test
    public void TestPdfReader() throws IOException {
        File File = new File("TestDigitalSignatureFactory-1.pdf");
        if (File.createNewFile()) {
            PdfWriter writter = new PdfWriter(File.getPath());
            PdfDocument doc = new PdfDocument(writter);
            doc.addNewPage();
            doc.close();
        }
        PdfWriter writter = new PdfWriter(File.getPath());
        PdfDocument doc = new PdfDocument(writter);
        doc.addNewPage();
        doc.close();
        PdfReader reader = DigitalSignatureFactory.getPdfReader(File.getPath());

        assertThat(reader, is(not(nullValue())));

        FileUtils.deleteQuietly(File);
    }

    @Test(expected = IOException.class)
    public void TestPdfReaderIOException() throws IOException {
        DigitalSignatureFactory.getPdfReader("abc");
    }

    @Test
    public void TestPdfSigner() throws IOException {
        File inFile = new File("TestDigitalSignatureFactory-2.pdf");
        File outFile = new File("TestDigitalSignatureFactory-out-2.pdf");
        if (inFile.createNewFile()) {
            PdfWriter writter = new PdfWriter(inFile.getPath());
            PdfDocument doc = new PdfDocument(writter);
            doc.addNewPage();
            doc.close();
        }

        PdfReader reader = DigitalSignatureFactory.getPdfReader(inFile.getPath());
        PdfSigner signer = DigitalSignatureFactory.getPdfSigner(reader,outFile);

        assertThat(signer, is(not(nullValue())));

        FileUtils.deleteQuietly(inFile);
        FileUtils.deleteQuietly(outFile);
    }

    @Test
    public void TestInitialize() {
        DigitalSignatureFactory factory = new DigitalSignatureFactory();
        assertThat(factory, is(not(nullValue())));
    }
}
