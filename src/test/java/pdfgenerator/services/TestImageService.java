package pdfgenerator.services;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import mapimage.MapImageService;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.models.Feature;
import pdfgenerator.exceptions.ImageGenerationException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
    ImageService.class,
    MapImageService.class,
    ImageIO.class,
    ImageDataFactory.class,
    Image.class
})
public class TestImageService extends EasyMockSupport {

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
    public void TestImageServiceImageReturned() throws Exception {
        PowerMock.mockStatic(MapImageService.class);
        PowerMock.mockStatic(ImageIO.class);
        PowerMock.mockStatic(ImageDataFactory.class);
        ImageIO imageIOMock = PowerMock.createMock(ImageIO.class);
        ImageDataFactory idfMock = PowerMock.createMock(ImageDataFactory.class);
        MapImageService mapImageServiceMock = mock(MapImageService.class);
        BufferedImage biMock = mock(BufferedImage.class);
        ByteArrayOutputStream baosMock = mock(ByteArrayOutputStream.class);
        byte[] byteArray = new byte[0];
        ImageData imageDataMock = mock(ImageData.class);
        Image imageMock = mock(Image.class);

        EasyMock.expect(MapImageService.getInstance())
                .andReturn(mapImageServiceMock)
                .once();
        EasyMock.expect(mapImageServiceMock.generateImage(EasyMock.<List<Feature>> anyObject(), EasyMock.anyInt(), EasyMock.anyInt()))
                .andReturn(biMock)
                .once();
        PowerMock.expectNew(ByteArrayOutputStream.class)
                .andReturn(baosMock)
                .once();
        EasyMock.expect(imageIOMock.write(EasyMock.anyObject(BufferedImage.class), EasyMock.anyString(), EasyMock.anyObject(ByteArrayOutputStream.class)))
                .andReturn(true)
                .once();
        EasyMock.expect(baosMock.toByteArray())
                .andReturn(byteArray)
                .once();
        EasyMock.expect(idfMock.create(EasyMock.anyObject(byte[].class)))
                .andReturn(imageDataMock)
                .once();
        PowerMock.expectNew(Image.class, imageDataMock)
                .andReturn(imageMock)
                .once();

        Feature feature1 = mock(Feature.class);
        Feature feature2 = mock(Feature.class);
        List<Feature> featureCollection = Arrays.asList(feature1, feature2);

        replayAll();

        Image image = ImageService.generate(featureCollection);

        verifyAll();

        assertThat(image, equalTo(imageMock));
    }

    @Test(expected = ImageGenerationException.class)
    public void TestImageServiceExceptionThrown() throws Exception {
        PowerMock.mockStatic(MapImageService.class);
        MapImageService mapImageServiceMock = mock(MapImageService.class);

            EasyMock.expect(mapImageServiceMock.getInstance())
                    .andThrow(new ImageGenerationException("Test exception"));

        Feature feature1 = mock(Feature.class);
        Feature feature2 = mock(Feature.class);
        List<Feature> featureCollection = Arrays.asList(feature1, feature2);

        replayAll();

        ImageService.generate(featureCollection);
    }
}
