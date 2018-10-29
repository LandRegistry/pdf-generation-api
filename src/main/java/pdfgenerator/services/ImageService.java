package pdfgenerator.services;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import mapimage.MapImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerationapi.models.Feature;
import pdfgenerator.exceptions.ImageGenerationException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * This service is responsible for generating images from a GeoJSON FeatureCollection.
 */
public final class ImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    // Image Dimensions:
    private static final Integer WIDTH = 200;
    private static final Integer HEIGHT = 150;

    private ImageService() {}

    /**
     * Generates and returns an image for the given {@link List<Feature>} object.
     * @param featureCollection A FeatureCollection to generate an image from.
     * @return An image representing the geometry defined in the given FeatureCollection.
     * @throws ImageGenerationException If an image could not be generated from the given FeatureCollection.
     */
    public static Image generate(List<Feature> featureCollection) throws ImageGenerationException {
        LOGGER.info("Generating image.");

        Image searchExtentImage;
        try {
            searchExtentImage = build(featureCollection);
        }
        catch (IOException e) {
            LOGGER.error("Failed to generate image.");
            LOGGER.debug("Failed to generate an image for the given FeatureCollection: {}", featureCollection);
            throw new ImageGenerationException("Failed to generate image.", e);
        }

        LOGGER.info("Successfully generated image.");
        return searchExtentImage;
    }

    /**
     * Generates and returns an image for the given {@link List<Feature>} collection.
     * @param featureCollection A FeatureCollection to generate an image from.
     * @return An image representing the geometry defined in the given FeatureCollection.
     * @throws MalformedURLException If the image location on disk was invalid.
     */
    private static Image build(List<Feature> featureCollection) throws IOException, ImageGenerationException {
        BufferedImage bufferedImage = MapImageService.getInstance().generateImage(featureCollection, WIDTH, HEIGHT);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        return new Image(ImageDataFactory.create(byteArrayOutputStream.toByteArray()));
    }

}
