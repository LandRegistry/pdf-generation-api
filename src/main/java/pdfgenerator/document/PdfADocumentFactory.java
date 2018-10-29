package pdfgenerator.document;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.pdfa.PdfADocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pdfgenerator.resources.Colours;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Builds and returns a configured instance of the {@link PdfADocument} class.
 */
final class PdfADocumentFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfADocumentFactory.class);

    private static final PdfAConformanceLevel CONFORMANCE_LEVEL = PdfAConformanceLevel.PDF_A_2A;

    private static final String OUTPUT_CONDITION_IDENTIFIER = "Custom";
    private static final String OUTPUT_CONDITION = "";
    private static final String REGISTRY_NAME = "http://www.color.org";
    private static final String INFO = "sRGB IEC61966-2.1";

    private static final String LANGUAGE = "en-UK";

    private static final String DOCUMENT_TITLE = "Local Land Charges Official Search Certificate";

    private PdfADocumentFactory() {}

    /**
     * Builds and returns a single configured instance of the {@link PdfADocument} class.
     * @param filename The file on disk to output to.
     * @return A configured instance of the {@link PdfADocument} class.
     * @throws FileNotFoundException If the given filename could not be written to.
     */
    static PdfADocument build(String filename) throws IOException {
        LOGGER.debug("Building PdfADocument instance.");

        PdfOutputIntent pdfOutputIntent =
            new PdfOutputIntent(OUTPUT_CONDITION_IDENTIFIER, OUTPUT_CONDITION, REGISTRY_NAME, INFO, Colours.getInstance().getColour());

        PdfADocument pdf = new PdfADocument(new PdfWriter(filename), CONFORMANCE_LEVEL, pdfOutputIntent);
        pdf.setTagged();
        pdf.getCatalog().setLang(new PdfString(LANGUAGE));
        pdf.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
        pdf.getDocumentInfo().setTitle(DOCUMENT_TITLE);

        return pdf;
    }
}
