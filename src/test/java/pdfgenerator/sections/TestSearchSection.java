package pdfgenerator.sections;

import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.layout.element.*;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.TestUtils;
import pdfgenerationapi.models.Llc1PdfRequest;
import pdfgenerator.exceptions.ImageGenerationException;
import pdfgenerator.models.LocalLandCharge;
import pdfgenerator.services.ImageService;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageService.class})
public class TestSearchSection extends EasyMockSupport {

    @Before
    public void initialize() throws Exception {
        PowerMock.mockStatic(ImageService.class);
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
    public void testGenerate() throws IOException, ImageGenerationException {
        Llc1PdfRequest request = TestUtils.buildLlcRequest();
        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);
        EasyMock.expect(properties.setAlternateDescription("Map showing searched area")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);

        EasyMock.expect(ImageService.generate(request.getExtents().getFeatures())).andReturn(image);
        replayAll();

        Table table = SearchSection.generate(request);
        Table searchOuterTable = (Table)table.getCell(1, 0).getChildren().get(0);
        Table searchDetailsInnerTable = (Table)searchOuterTable.getCell(0, 0).getChildren().get(0);
        Table searchExtentInnerTable = (Table)searchOuterTable.getCell(0, 1).getChildren().get(0);

        String searchSectionCertifiedText = ((Text)((Paragraph)table.getCell(0, 0).getChildren().get(0)).getChildren().get(0)).getText();
        String timeAndDateHeading = ((Text)((Paragraph)searchDetailsInnerTable.getCell(0, 0).getChildren().get(0)).getChildren().get(0)).getText();
        String referenceHeading = ((Text)((Paragraph)searchDetailsInnerTable.getCell(2, 0).getChildren().get(0)).getChildren().get(0)).getText();
        String referenceContent = ((Text)((Paragraph)searchDetailsInnerTable.getCell(3, 0).getChildren().get(0)).getChildren().get(0)).getText();
        String searchAreaDescriptionHeading = ((Text)((Paragraph)searchDetailsInnerTable.getCell(4, 0).getChildren().get(0)).getChildren().get(0)).getText();
        String searchAreaDescriptionContent = ((Text)((Paragraph)searchDetailsInnerTable.getCell(5, 0).getChildren().get(0)).getChildren().get(0)).getText();

        String chargeExtentText = ((Text)((Paragraph)searchExtentInnerTable.getCell(0, 0).getChildren().get(1)).getChildren().get(0)).getText();

        assertThat(table, is(not(nullValue())));
        assertThat(searchOuterTable, is(not(nullValue())));
        assertThat(searchDetailsInnerTable, is(not(nullValue())));
        assertThat(searchExtentInnerTable, is(not(nullValue())));

        assertThat(searchSectionCertifiedText, is("It is hereby certified that the search of land and property as shown below reveals registrations up to and including the date and time of this certificate"));
        assertThat(timeAndDateHeading, is("Time and date"));
        assertThat(referenceHeading, is("Reference"));
        assertThat(referenceContent, is("000 000 012"));
        assertThat(searchAreaDescriptionHeading, is("Description of search area"));
        assertThat(searchAreaDescriptionContent, is(request.getDescription()));

        assertThat(chargeExtentText, is("Search area is outlined on the map"));

        verifyAll();
    }

    @Test
    public void testFormattingAndHeaders() throws IOException, ImageGenerationException {
        Llc1PdfRequest request = TestUtils.buildLlcRequest();
        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);
        EasyMock.expect(properties.setAlternateDescription("Map showing searched area")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);

        EasyMock.expect(ImageService.generate(request.getExtents().getFeatures())).andReturn(image);
        replayAll();

        Table table = SearchSection.generate(request);
        Table searchOuterTable = (Table)table.getCell(1, 0).getChildren().get(0);
        Table searchDetailsInnerTable = (Table)searchOuterTable.getCell(0, 0).getChildren().get(0);
        Table searchExtentInnerTable = (Table)searchOuterTable.getCell(0, 1).getChildren().get(0);

        assertThat(table, is(not(nullValue())));
        assertThat(searchOuterTable, is(not(nullValue())));
        assertThat(searchDetailsInnerTable, is(not(nullValue())));
        assertThat(searchExtentInnerTable, is(not(nullValue())));

        assertThat(table.getColumnWidth(0).getValue(), is(100f));
        assertThat(searchOuterTable.getColumnWidth(0).getValue(), is(55f));
        assertThat(searchOuterTable.getColumnWidth(1).getValue(), is(45f));
        assertThat(searchDetailsInnerTable.getColumnWidth(0).getValue(), is(100f));
        assertThat(searchExtentInnerTable.getColumnWidth(0).getValue(), is(100f));

        String tableHeader = ((Text)((Paragraph)table.getHeader().getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String searchOuterTableLeftColHeader = ((Text)((Paragraph)searchOuterTable.getHeader().getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String searchOuterTableRightColHeader = ((Text)((Paragraph)searchOuterTable.getHeader().getCell(0,1).getChildren().get(0)).getChildren().get(0)).getText();
        String searchDetailsInnerTableHeader = ((Text)((Paragraph)searchDetailsInnerTable.getHeader().getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String searchExtentInnerTableHeader = ((Text)((Paragraph)searchExtentInnerTable.getHeader().getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();

        assertThat(tableHeader, is("Search extent section"));
        assertThat(searchOuterTableLeftColHeader, is("Search details outer column"));
        assertThat(searchOuterTableRightColHeader, is("Search extent outer column"));
        assertThat(searchDetailsInnerTableHeader, is("Search details inner column"));
        assertThat(searchExtentInnerTableHeader, is("Search extent inner column"));
    }

    @Test
    public void testReferenceNumberFormatting() throws IOException, ImageGenerationException {
        Llc1PdfRequest request = TestUtils.buildLlcRequest();

        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);
        EasyMock.expect(properties.setAlternateDescription("Map showing searched area")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);

        EasyMock.expect(ImageService.generate(request.getExtents().getFeatures())).andReturn(image);
        replayAll();

        request.setReferenceNumber(123123123);
        Table table = SearchSection.generate(request);

        Table searchOuterTable = (Table)table.getCell(1, 0).getChildren().get(0);
        Table searchDetailsInnerTable = (Table)searchOuterTable.getCell(0, 0).getChildren().get(0);
        String referenceText = ((Text)((Paragraph)searchDetailsInnerTable.getCell(3, 0).getChildren().get(0)).getChildren().get(0)).getText();

        assertThat(referenceText, is("123 123 123"));

        verifyAll();
    }
}
