package pdfgenerator.document;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerator.document.WatermarkEventHandler;
import pdfgenerator.sections.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
    WatermarkSection.class,
    WatermarkEventHandler.class
})
public class TestWatermarkEventHandler extends EasyMockSupport {

    @Before
    public void initialize() throws Exception {
        PowerMock.mockStatic(WatermarkSection.class);
    }

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
    	Paragraph paragraph = mock(Paragraph.class);
    	Canvas canvas = mock(Canvas.class);
    	Rectangle pageSize = new Rectangle(12, 12);
	
    	EasyMock.expect(docEvent.getDocument()).andReturn(pdfDoc);
    	EasyMock.expect(docEvent.getPage()).andReturn(pdfPage);
    	PowerMock.expectNew(PdfCanvas.class, pdfPage).andReturn(pdfCanvas);
    	EasyMock.expect(pdfDoc.getPageNumber(pdfPage)).andReturn(1);
    	EasyMock.expect(pdfPage.getPageSize()).andReturn(pageSize);
        EasyMock.expect(WatermarkSection.generate(EasyMock.anyString())).andReturn(paragraph);
    	PowerMock.expectNew(Canvas.class, pdfCanvas, pdfDoc, pageSize).andReturn(canvas);
    	EasyMock.expect(canvas.showTextAligned(EasyMock.anyObject(Paragraph.class), EasyMock.anyFloat(), EasyMock.anyFloat(),EasyMock.anyInt(), 
    			EasyMock.anyObject(TextAlignment.class), EasyMock.anyObject(VerticalAlignment.class), EasyMock.anyFloat())).andReturn(canvas).times(2);
    	canvas.close();
        EasyMock.expectLastCall();
        pdfCanvas.release();
        EasyMock.expectLastCall();
    }

    @Test
    public void testParagraphException() throws Exception {
    }

}