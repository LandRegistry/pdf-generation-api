package pdfgenerator.sections;

import com.itextpdf.layout.element.Paragraph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerator.utility.ParagraphBuilder;

import java.io.IOException;

/**
 * Class responsible for generating the copyright section of the PDF.
 */
public final class WatermarkSection {
    private static final Logger LOGGER = LoggerFactory.getLogger(WatermarkSection.class);

    private static final float OPACITY = 0.05f;
    private static final float ROTATION_ANGLE = 0.60f;

    private WatermarkSection() {}

    /**
     * Builds and returns the Watermark Section of the LLC1 PDF.
     * @param 
     * @return The Watermark Section of the LLC1 PDF.
     * @throws IOException If the font file could not be accessed.
     */
    public static Paragraph generate(String content) throws IOException {
        LOGGER.debug("Generating watermark section.");

        return ParagraphBuilder.watermark(content)
        		.setOpacity(OPACITY)
        		.setRotationAngle(ROTATION_ANGLE);
    }
}
