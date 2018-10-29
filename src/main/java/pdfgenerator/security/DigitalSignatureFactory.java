package pdfgenerator.security;

import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.PdfSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Factory class responsible for the creation of PDFReaders and PDFSigners
 */
class DigitalSignatureFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(DigitalSignatureFactory.class);

    /**
     * Create a new PdfReader
     * @param path Loaction of the PDF file the reader is to read
     * @return a new instance of a PdfReader
     * @throws IOException If the file can not be found
     */
    static PdfReader getPdfReader(String path) throws IOException{
        LOGGER.debug("Building PDF reader");
        return new PdfReader(path);
    }

    /**
     * Create a new PdfSigner
     * @param reader PdfReader for the file to be signed
     * @param outFile The file the signer should right the signed document to
     * @return new instance of a PdfSigner
     * @throws IOException if the file to write to can not be found
     */
    static PdfSigner getPdfSigner(PdfReader reader, File outFile) throws IOException {
        LOGGER.debug("Building PDF signer");
        return new PdfSigner(reader, new FileOutputStream(outFile), true);
    }
}
