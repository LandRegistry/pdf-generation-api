package pdfgenerator.concurrency;

import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
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
import pdfgenerationapi.Config;
import pdfgenerationapi.TestUtils;
import pdfgenerator.exceptions.ImageGenerationException;
import pdfgenerator.models.LocalLandCharge;
import pdfgenerator.sections.LlcSection;
import pdfgenerator.sections.LonSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Config.class,
        GenerateChargeImageCallableFactory.class,
        GenerateChargeImageCallable.class,
        Image.class,
        LlcSection.class,
        LonSection.class
})
public class TestChargeSectionBuilder extends EasyMockSupport {
    @BeforeClass
    public static void suitSetup() throws Exception {
        TestUtils.SetupEnvironment();
    }

    @Before
    public void initialize() {
        PowerMock.mockStatic(GenerateChargeImageCallableFactory.class);
        PowerMock.mockStatic(LlcSection.class);
        PowerMock.mockStatic(LonSection.class);
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
    public void testStartThreads1() throws ImageGenerationException, ExecutionException, InterruptedException {
        runStartThreadsTest(1);
    }

    @Test
    public void testStartThreads4() throws ImageGenerationException, ExecutionException, InterruptedException {
        runStartThreadsTest(4);
    }

    @Test
    public void testStartThreads16() throws ImageGenerationException, ExecutionException, InterruptedException {
        runStartThreadsTest(16);
    }

    @Test
    public void testStartThreads32() throws ImageGenerationException, ExecutionException, InterruptedException {
        runStartThreadsTest(32);
    }

    @Test
    public void testAppendSectionsToDocument1() throws InterruptedException, ExecutionException, ImageGenerationException, IOException {
        List<LocalLandCharge> charges = new ArrayList<>();
        charges.add(TestUtils.buildLocalLandCharge());

        EasyMock.expect(LlcSection.generate(EasyMock.anyObject(LocalLandCharge.class), EasyMock.anyObject(Image.class)))
                .andReturn(TABLE)
                .once();

        runAppendSectionsToDocumentTest(charges);
    }

    @Test
    public void testAppendSectionsToDocument4() throws InterruptedException, ExecutionException, ImageGenerationException, IOException {
        runAppendSectionsToDocumentTest(buildCharges(4));
    }

    @Test
    public void testAppendSectionsToDocument16() throws InterruptedException, ExecutionException, ImageGenerationException, IOException {
        runAppendSectionsToDocumentTest(buildCharges(16));
    }

    @Test
    public void testAppendSectionsToDocument32() throws InterruptedException, ExecutionException, ImageGenerationException, IOException {
        runAppendSectionsToDocumentTest(buildCharges(32));
    }

    public void runStartThreadsTest(int chargeCount) throws ImageGenerationException, ExecutionException, InterruptedException {
        List<LocalLandCharge> charges = new ArrayList<>();

        Image image = mock(Image.class);
        GenerateChargeImageCallable generateChargeImageMock = partialMockBuilder(GenerateChargeImageCallable.class)
                .addMockedMethod("call")
                .createMock();

        EasyMock.expect(generateChargeImageMock.call())
                .andReturn(image)
                .times(chargeCount);
        EasyMock.expect(GenerateChargeImageCallableFactory.getGenerateChargeImageCallable(EasyMock.anyObject(LocalLandCharge.class)))
                .andReturn(generateChargeImageMock)
                .times(chargeCount);

        replayAll();

        for (int i = 0; i < chargeCount; i++) {
            charges.add(TestUtils.buildLocalLandCharge());
        }

        List<Future<Image>> futures = ChargeSectionBuilder.startThreads(charges);

        assertThat(futures.size(), is(chargeCount));

        for (Future<Image> future : futures) {
            assertNotNull(future.get());
        }

        verifyAll();
    }

    public void runAppendSectionsToDocumentTest(List<LocalLandCharge> charges) throws InterruptedException, ExecutionException, ImageGenerationException, IOException {
        // Expected sections include line separators, first charge doesn't draw line separator
        int expectedSectionCount = (charges.size() * 2) - 1;
        List<Future<Image>> futures = new ArrayList<>();

        Document documentMock = mock(Document.class);
        Image image = mock(Image.class);
        Future<Image> future = mock(Future.class);

        EasyMock.expect(documentMock.add(EasyMock.anyObject(IBlockElement.class)))
                .andReturn(documentMock)
                .times(expectedSectionCount);
        EasyMock.expect(future.isDone())
                .andReturn(true)
                .times(charges.size());
        EasyMock.expect(future.get())
                .andReturn(image)
                .times(charges.size());

        for (int i = 0; i < charges.size(); i++) {
            futures.add(future);
        }

        replayAll();

        ChargeSectionBuilder.appendSectionsToDocument(futures, charges, documentMock);

        for (Future<Image> fut : futures) {
            assertNull(fut);
        }

        verifyAll();
    }

    /**
     * Builds list of charges containing equal amounts of LLC and LON charges
     * @param chargeCount Expected charge count
     * @return A list of charges
     */
    private List<LocalLandCharge> buildCharges(int chargeCount) throws IOException, ImageGenerationException {
        List<LocalLandCharge> charges = new ArrayList<>();

        for (int i = 0; i < chargeCount / 2; i++) {
            LocalLandCharge charge = TestUtils.buildLocalLandCharge();
            LocalLandCharge lon = TestUtils.buildLONCharge();
            charges.add(charge);
            charges.add(lon);
        }

        EasyMock.expect(LlcSection.generate(EasyMock.anyObject(LocalLandCharge.class), EasyMock.anyObject(Image.class)))
                .andReturn(TABLE)
                .times(chargeCount / 2);
        EasyMock.expect(LonSection.generate(EasyMock.anyObject(LocalLandCharge.class), EasyMock.anyObject(Image.class)))
                .andReturn(TABLE)
                .times(chargeCount / 2);

        return charges;
    }
}
