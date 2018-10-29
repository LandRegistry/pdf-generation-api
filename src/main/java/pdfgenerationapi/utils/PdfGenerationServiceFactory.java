package pdfgenerationapi.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pdfgenerationapi.services.LLC1.IPdfGenerationService;
import pdfgenerationapi.services.LLC1.llc1PdfGenerationService;

public class PdfGenerationServiceFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfGenerationServiceFactory.class);

    public static IPdfGenerationService getPdfGenerationInstance(String type) {
        switch (type) {
            case "llc1":
                return new llc1PdfGenerationService();
            default:
                LOGGER.error("Type " + type + " is not a valid pdf generation service");
                throw new IllegalArgumentException("Type " + type + " unknown");
        }
    }
}
