package pdfgenerator.document;

import com.itextpdf.layout.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Builds and returns a configured instance of the {@link Document} class.
 */
public final class DocumentFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFactory.class);

    private static final float MARGIN = 36;
    private static final float TOP_MARGIN = 12.7f;
    private static final float BOTTOM_MARGIN = 80;

    private DocumentFactory() {}

    /**
     * Builds and returns a configured instance of the {@link Document} class.
     * @param filename The file on disk to output to.
     * @return A configured instance of the {@link Document} class.
     * @throws FileNotFoundException If the given filename could not be written to.
     */
    public static Document build(String filename) throws IOException {
        LOGGER.debug("Building Document instance.");

        Document document = new Document(PdfADocumentFactory.build(filename));
        document.setMargins(TOP_MARGIN, MARGIN, BOTTOM_MARGIN, MARGIN);
        return document;
    }
}
