package pdfgenerator.document;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.TextAlignment;
import pdfgenerator.resources.Fonts;
import pdfgenerator.sections.CopyrightSection;

import java.io.IOException;

public class FooterEventHandler implements IEventHandler {
    protected Document doc;

    public FooterEventHandler(Document doc) {
        this.doc = doc;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfPage page = docEvent.getPage();
        PdfDocument doc = docEvent.getDocument();
        Rectangle pageSize = page.getPageSize();
        int pageNum = doc.getPageNumber(page);

        PdfFont font = null;
        try {
            font = Fonts.getInstance().getTransFont();
        } catch (IOException e) {
            e.printStackTrace();
        }
        float x = pageSize.getLeft() + ((pageSize.getRight() - pageSize.getLeft()) / 2);
        float y = 80;

        PdfCanvas pdfCanvas = new PdfCanvas(page);
        pdfCanvas.beginText()
                .setFontAndSize(font, 12)
                .moveText(x, y)
                .showText(String.format("%d", pageNum))
                .endText();

        Canvas canvas = new Canvas(pdfCanvas, doc, pageSize);
        try {
            canvas.showTextAligned(CopyrightSection.generate(), 30, 10, TextAlignment.LEFT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfCanvas.release();
    }
}