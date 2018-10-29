package pdfgenerator.core;

import com.google.common.collect.Lists;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.TestUtils;
import pdfgenerationapi.models.Llc1GenerationResult;
import pdfgenerationapi.models.Llc1PdfRequest;
import pdfgenerator.concurrency.ChargeSectionBuilder;
import pdfgenerator.document.DocumentFactory;
import pdfgenerator.document.FooterEventHandler;
import pdfgenerator.document.WatermarkEventHandler;
import pdfgenerator.exceptions.*;
import pdfgenerator.models.LocalLandCharge;
import pdfgenerator.sections.*;
import pdfgenerator.security.DigitalSignature;
import pdfgenerator.services.SearchService;
import pdfgenerator.services.StorageService;
import pdfgenerator.models.StorageResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
    SearchService.class,
    StorageService.class,
    DocumentFactory.class,
    CopyrightSection.class,
    HeadingSection.class,
    ChargeCountSection.class,
    SearchSection.class,
    LineSeparatorSection.class,
    DigitalSignature.class,
    ChargeSectionBuilder.class
})
public class TestLLC1 extends EasyMockSupport {
    @BeforeClass
    public static void suitSetup() throws Exception {
        TestUtils.SetupEnvironment();
    }

    @Before
    public void initialize() {
        PowerMock.mockStatic(SearchService.class);
        PowerMock.mockStatic(StorageService.class);
        PowerMock.mockStatic(DocumentFactory.class);
        PowerMock.mockStatic(CopyrightSection.class);
        PowerMock.mockStatic(HeadingSection.class);
        PowerMock.mockStatic(ChargeCountSection.class);
        PowerMock.mockStatic(SearchSection.class);
        PowerMock.mockStatic(LineSeparatorSection.class);
        PowerMock.mockStatic(DigitalSignature.class);
        PowerMock.mockStatic(ChargeSectionBuilder.class);
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

    private static final Table TABLE = new Table(new UnitValue[] {
        UnitValue.createPercentValue(80),
        UnitValue.createPercentValue(20)
    });

    @Test
    public void testSuccessfulCreation() throws Exception {
        LocalLandCharge lightObstructionNotice = TestUtils.buildLONCharge();
        LocalLandCharge localLandCharge = TestUtils.buildLocalLandCharge();

        testGenerate(Lists.newArrayList(localLandCharge, lightObstructionNotice));
    }

    /**
     * Should process the Local Land Charges in descending order (i.e. latest charge first) based on their charge
     * creation date.
     */
    @Test
    public void testSearch() throws Exception {
        LocalLandCharge localLandChargeOne = TestUtils.buildLocalLandCharge();
        localLandChargeOne.getItem().setChargeCreationDate(new Date(1));

        LocalLandCharge localLandChargeTwo = TestUtils.buildLocalLandCharge();
        localLandChargeTwo.getItem().setChargeCreationDate(new Date(3));

        LocalLandCharge localLandChargeThree = TestUtils.buildLocalLandCharge();
        localLandChargeThree.getItem().setChargeCreationDate(new Date(2));

        LocalLandCharge lightObstructionNotice = TestUtils.buildLONCharge();
        localLandChargeTwo.getItem().setChargeCreationDate(new Date(4));

        testGenerate(Lists.newArrayList(localLandChargeOne, localLandChargeTwo, localLandChargeThree,
                lightObstructionNotice));
    }

    private void testGenerate(List<LocalLandCharge> localLandCharges) throws Exception {
        Document doc = mock(Document.class);
        PdfDocument pdfDoc = mock(PdfDocument.class);
        Llc1PdfRequest request = TestUtils.buildLlcRequest();
        Paragraph paragraph = new Paragraph();
        File signedFile = mock(File.class);
        LineSeparator lineSeparator = mock(LineSeparator.class);
        List<Future<Image>> futures = new ArrayList<>();

        EasyMock.expect(signedFile.exists()).andReturn(true);
        EasyMock.expect(signedFile.isDirectory()).andReturn(false);
        EasyMock.expect(signedFile.delete()).andReturn(true);
        EasyMock.expect(SearchService.get(request)).andReturn(localLandCharges);
        EasyMock.expect(ChargeSectionBuilder.startThreads(localLandCharges)).andReturn(futures);
        EasyMock.expect(DigitalSignature.sign(EasyMock.anyObject(File.class))).andReturn(signedFile);
        StorageResult storageResult = new StorageResult("abc", "external-link");
        EasyMock.expect(StorageService.save(EasyMock.anyObject(File.class))).andReturn(storageResult);
        EasyMock.expect(DocumentFactory.build(EasyMock.anyString())).andReturn(doc);
        EasyMock.expect(HeadingSection.generate()).andReturn(TABLE);
        EasyMock.expect(ChargeCountSection.generate(EasyMock.anyInt())).andReturn(TABLE);
        ChargeSectionBuilder.appendSectionsToDocument(futures, localLandCharges, doc);
        EasyMock.expectLastCall();
        EasyMock.expect(SearchSection.generate(EasyMock.anyObject(Llc1PdfRequest.class))).andReturn(TABLE);
        EasyMock.expect(CopyrightSection.generate()).andReturn(paragraph);    
        EasyMock.expect(LineSeparatorSection.generate()).andReturn(lineSeparator).anyTimes();
        EasyMock.expect(doc.add(TABLE)).andReturn(doc).anyTimes();
        EasyMock.expect(doc.add(paragraph)).andReturn(doc).anyTimes();
        EasyMock.expect(doc.add(lineSeparator)).andReturn(doc).anyTimes();
        EasyMock.expect(doc.getPdfDocument()).andReturn(pdfDoc).anyTimes();
        pdfDoc.addEventHandler(EasyMock.anyString(), EasyMock.anyObject(WatermarkEventHandler.class));
        EasyMock.expectLastCall();
        pdfDoc.addEventHandler(EasyMock.anyString(), EasyMock.anyObject(FooterEventHandler.class));
        EasyMock.expectLastCall();
        doc.close();
        EasyMock.expectLastCall();

        replayAll();

        LLC1 llc1 =  new LLC1();
        Llc1GenerationResult result = llc1.generate(request);

        assertThat(result, is(not(nullValue())));
        assertThat(result.getDocumentUrl(), is("abc"));
    }

    @Test(expected = PdfGenerationException.class)
    public void testSearchException() throws Exception {
        Llc1PdfRequest request = TestUtils.buildLlcRequest();

        EasyMock.expect(SearchService.get(request)).andThrow(new SearchException("error"));

        replayAll();

        LLC1 llc1 =  new LLC1();
        llc1.generate(request);
    }
}