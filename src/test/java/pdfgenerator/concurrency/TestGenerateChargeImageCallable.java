package pdfgenerator.concurrency;

import com.itextpdf.layout.element.Image;
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
import pdfgenerator.exceptions.ImageGenerationException;
import pdfgenerator.models.LocalLandCharge;
import pdfgenerator.sections.LlcSection;
import pdfgenerator.sections.LonSection;
import pdfgenerator.services.ImageService;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
    ImageService.class
})
public class TestGenerateChargeImageCallable extends EasyMockSupport {
    @Before
    public void initialize() {
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
    public void testCall() throws ImageGenerationException {
        Image image = mock(Image.class);
        LocalLandCharge charge = TestUtils.buildLocalLandCharge();
        GenerateChargeImageCallable callable = new GenerateChargeImageCallable(charge);
        EasyMock.expect(ImageService.generate(charge.getGeometry().getFeatures()))
                .andReturn(image);

        replayAll();

        callable.call();

        verifyAll();
    }
}
