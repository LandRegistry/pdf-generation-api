package pdfgenerator.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import pdfgenerationapi.Config;
import pdfgenerator.sections.WatermarkSection;

/**
 * This is an event handler which will add a watermark to a pdf document
 */
public class WatermarkEventHandler implements IEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatermarkEventHandler.class);
    
    private List<Integer> watermarkedPages;
    
    public WatermarkEventHandler() {
    	super();
    	watermarkedPages = new ArrayList<Integer>();
    }
	
	public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent)event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage pdfPage = docEvent.getPage();
        PdfCanvas pdfCanvas = new PdfCanvas(pdfPage);
        int pageNumber = pdfDoc.getPageNumber(pdfPage);
        // Workaround because END_PAGE event seems to fire twice on page 1 sometimes
        if (watermarkedPages.contains(new Integer(pageNumber))) {
        	LOGGER.debug("Already watermarked this page, skipping");
        	return;
        }
        watermarkedPages.add(new Integer(pageNumber));
        Rectangle pagesize = pdfPage.getPageSize();
        float x = (pagesize.getLeft() + pagesize.getRight()) / 2;
        float y = (pagesize.getTop() + pagesize.getBottom());
        Paragraph watermark = null;
		try {
			watermark = WatermarkSection.generate(Config.WATERMARK);
		} catch (IOException e) {
			LOGGER.error("Unable to create watermark paragraph");
		}
        Canvas canvas = new Canvas(pdfCanvas, pdfDoc, pdfPage.getPageSize());
        canvas.showTextAligned(watermark, x, y / 12, pageNumber, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
        canvas.showTextAligned(watermark, x, (2 * y) / 3, pageNumber, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
        canvas.close();
        pdfCanvas.release();
    }
}