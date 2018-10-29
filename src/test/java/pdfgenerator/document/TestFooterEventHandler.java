package pdfgenerator.document;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.Document;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
    FooterEventHandler.class
})
public class TestFooterEventHandler extends EasyMockSupport {

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
    public void testSuccessfulEvent() throws Exception {
    	PdfDocumentEvent docEvent = mock(PdfDocumentEvent.class);
    	PdfDocument pdfDoc = mock(PdfDocument.class);
    	PdfPage pdfPage = mock(PdfPage.class);
    	PdfCanvas pdfCanvas = mock(PdfCanvas.class);
    	Rectangle pageSize = new Rectangle(12, 12);
        Canvas canvas = mock(Canvas.class);


    	EasyMock.expect(docEvent.getPage()).andReturn(pdfPage);
    	EasyMock.expect(docEvent.getDocument()).andReturn(pdfDoc);
    	EasyMock.expect(pdfPage.getPageSize()).andReturn(pageSize);
    	EasyMock.expect(pdfDoc.getPageNumber(pdfPage)).andReturn(1);
    	PowerMock.expectNew(PdfCanvas.class, pdfPage).andReturn(pdfCanvas);
    	EasyMock.expect(pdfCanvas.beginText()).andReturn(pdfCanvas);
    	EasyMock.expect(pdfCanvas.setFontAndSize(EasyMock.anyObject(PdfFont.class), EasyMock.anyFloat())).andReturn(pdfCanvas);
    	EasyMock.expect(pdfCanvas.moveText(EasyMock.anyDouble(), EasyMock.anyDouble())).andReturn(pdfCanvas);
    	EasyMock.expect(pdfCanvas.showText(EasyMock.anyString())).andReturn(pdfCanvas);
    	EasyMock.expect(pdfCanvas.endText()).andReturn(pdfCanvas);
        PowerMock.expectNew(Canvas.class, pdfCanvas, pdfDoc, pageSize).andReturn(canvas);
        EasyMock.expect(canvas.showTextAligned(EasyMock.anyObject(Paragraph.class), EasyMock.anyFloat(), EasyMock.anyFloat(), EasyMock.anyObject(TextAlignment.class))).andReturn(canvas);
        pdfCanvas.release();
        EasyMock.expectLastCall();

        replayAll();

        FooterEventHandler handler = new FooterEventHandler(mock(Document.class));
        handler.handleEvent(docEvent);
    }

}