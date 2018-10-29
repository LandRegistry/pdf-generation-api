package pdfgenerator.core;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.layout.Document;

import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pdfgenerationapi.models.Llc1PdfRequest;
import pdfgenerationapi.Config;
import pdfgenerationapi.models.Llc1GenerationResult;
import pdfgenerator.concurrency.ChargeSectionBuilder;
import pdfgenerator.concurrency.GenerateChargeImageCallable;
import pdfgenerator.document.DocumentFactory;
import pdfgenerator.document.FooterEventHandler;
import pdfgenerator.document.WatermarkEventHandler;
import pdfgenerator.exceptions.*;
import pdfgenerator.models.LocalLandCharge;
import pdfgenerator.models.StorageResult;
import pdfgenerator.resources.Fonts;
import pdfgenerator.sections.*;
import pdfgenerator.security.DigitalSignature;
import pdfgenerator.services.SearchService;
import pdfgenerator.services.StorageService;

/**
 * This is the main class for the PDF generation module.
 */
public class LLC1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(LLC1.class);

    private final String filename;

    public LLC1() {
        this.filename = buildFilename();
    }

    /**
     * Generate an LLC1 PDF/A-2 document for the given extent(s) detailed in the given Llc1PdfRequest.
     * @param llc1PdfRequest The request body passed to the llc1 endpoint.
     * @return A URL pointing to the generated PDFs location in the file store.
     * @throws PdfGenerationException If the PDF could not be generated.
     */
    public Llc1GenerationResult generate(Llc1PdfRequest llc1PdfRequest) throws PdfGenerationException {
        LOGGER.info("Starting generation of PDF.");
        long startTime = System.nanoTime();

        File file = new File(this.filename);
        File signedPdf = null;
        try {
            List<LocalLandCharge> localLandCharges = SearchService.get(llc1PdfRequest);
            writePDF(llc1PdfRequest, localLandCharges);
            signedPdf = DigitalSignature.sign(file);
            StorageResult storageResult = StorageService.save(signedPdf);
            return  new Llc1GenerationResult(storageResult.getDocumentUrl(), storageResult.getExternalUrl(), localLandCharges);
        }
        catch (Exception ex){
            LOGGER.error("Failed to generate LLC1 PDF.", ex);
            throw new PdfGenerationException("Failed to generate LLC1 PDF.", ex);
        }
        finally {
            long duration = (System.nanoTime() - startTime) / 1000000;
            LOGGER.info("PDF generation ran for '{}' milliseconds.", Long.toString(duration));
            LOGGER.debug("Removing local instance of PDF, filename: {}", this.filename);
            FileUtils.deleteQuietly(file);
            if (signedPdf != null && signedPdf.exists()) {
                FileUtils.deleteQuietly(signedPdf);
            }
        }
    }

    /**
     * Writes each of the sections of the PDF.
     * @param llc1PdfRequest The request body passed to the llc1 endpoint.
     * @param localLandCharges The Local Land Charges returned by the search-api.
     * @throws ImageGenerationException If image generation failed.
     * @throws IOException If writing to the PDF on disk failed.
     */
    private void writePDF(Llc1PdfRequest llc1PdfRequest, List<LocalLandCharge> localLandCharges) throws ImageGenerationException, IOException, ExecutionException, InterruptedException {
        Collections.sort(localLandCharges);

        List<Future<Image>> futures = ChargeSectionBuilder.startThreads(localLandCharges);

        Fonts.getInstance().initFonts();
        Document document = DocumentFactory.build(this.filename);
        
        if (Config.WATERMARK != null) {
        	document.getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, new WatermarkEventHandler());
        }

        document.getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler(document));

        document.add(HeadingSection.generate());
        document.add(SearchSection.generate(llc1PdfRequest));
        document.add(ChargeCountSection.generate(localLandCharges.size()));

        ChargeSectionBuilder.appendSectionsToDocument(futures, localLandCharges, document);

        document.close();
    }

    /**
     * Builds and returns a uuid filename with a .pdf extension.
     * @return A uuid filename with a .pdf extension.
     */
    private static String buildFilename() {
        String filename = UUID.randomUUID() + ".pdf";
        LOGGER.info("Generated filename: {}", filename);
        return filename;
    }
}
