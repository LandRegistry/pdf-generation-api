package pdfgenerator.resources;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Builds instances of each of the images used to generate the LLC1 PDF.
 */
public final class Images {
    private static final Logger LOGGER = LoggerFactory.getLogger(Images.class);

    private static Images s_instance;

    private final byte[] landRegistryLogo;
    private final byte[] mediumGrayBar;
    private final byte[] largeGrayBar;
    private final byte[] placeholderImage;

    private static final String LAND_REGISTRY_LOGO_FILE_PATH = "images/land_registry_Logo.jpg";
    private static final String MEDIUM_GRAY_BAR_FILE_PATH = "images/medium_gray_bar.jpg";
    private static final String LARGE_GRAY_BAR_FILE_PATH = "images/large_gray_bar.jpg";
    private static final String PLACEHOLDER_FILE_PATH = "images/placeholder_search_extent.png";

    private static final Integer LOGO_WIDTH = 65;
    private static final Integer LOGO_HEIGHT = 65;
    private static final Integer LOGO_TOP_MARGIN = 15;

    private Images() throws IOException {
        this.landRegistryLogo = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream(LAND_REGISTRY_LOGO_FILE_PATH));
        this.mediumGrayBar = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream(MEDIUM_GRAY_BAR_FILE_PATH));
        this.largeGrayBar = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream(LARGE_GRAY_BAR_FILE_PATH));
        this.placeholderImage = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream(PLACEHOLDER_FILE_PATH));
    }

    /**
     * Builds and returns a singleton instance of this class.
     * @return A singleton instance of this class.
     * @throws IOException If any of the image resources could not be read.
     */
    public static synchronized Images getInstance() throws IOException {
        if (s_instance == null) {
            s_instance = new Images();
        }
        return s_instance;
    }

    /**
     * Builds and returns a Land Registry logo {@link Image} instance.
     * Configured to be the correct size for the document.
     * @return A Land Registry logo {@link Image} instance.
     */
    public Image getLogo() {
        LOGGER.debug("Built new logo image.");
        Image image = new Image(ImageDataFactory.create(landRegistryLogo))
            .scaleAbsolute(LOGO_WIDTH, LOGO_HEIGHT)
            .setMarginTop(LOGO_TOP_MARGIN)
            .setHorizontalAlignment(HorizontalAlignment.RIGHT);
        image.getAccessibilityProperties().setAlternateDescription("Land Registry Logo");
        return image;
    }

    /**
     * Builds and returns a medium gray bar {@link Image} instance.
     * @return A medium gray bar {@link Image} instance.
     */
    public Image getMediumGrayBar() {
        LOGGER.debug("Built new medium gray bar image.");
        Image image = new Image(ImageDataFactory.create(mediumGrayBar));
        image.getAccessibilityProperties().setAlternateDescription("Medium spacer");
        return image;
    }

    /**
     * Builds and returns a large gray bar {@link Image} instance.
     * @return A large gray bar {@link Image} instance.
     */
    public Image getLargeGrayBar() {
        LOGGER.debug("Built new large gray bar image.");
        Image image = new Image(ImageDataFactory.create(largeGrayBar));
        image.getAccessibilityProperties().setAlternateDescription("Large spacer");
        return image;
    }

    /**
     * Builds and returns a placeholder {@link Image} instance.
     * @return A placeholder {@link Image} instance.
     */
    public Image getPlaceholderImage() {
        LOGGER.debug("Built new Placeholder image.");
        Image image = new Image(ImageDataFactory.create(placeholderImage));
        image.getAccessibilityProperties().setAlternateDescription("Placeholder image");
        return image;
    }
}
