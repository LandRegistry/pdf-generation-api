package pdfgenerator.sections;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.element.LineSeparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Class responsible for generating the copyright section of the PDF.
 */
public final class LineSeparatorSection {
    private static final Logger LOGGER = LoggerFactory.getLogger(WatermarkSection.class);

    private static final int SEPARATOR_COLOR_RED = 168;
    private static final int SEPARATOR_COLOR_GREEN = 169;
    private static final int SEPARATOR_COLOR_BLUE = 173;

    private LineSeparatorSection() {}

    /**
     * Builds and returns the Line Separator Section of the LLC1 PDF.
     * @param 
     * @return The Line Separator Section of the LLC1 PDF.
     * @throws IOException If the font file could not be accessed.
     */
    public static LineSeparator generate() throws IOException {
        LOGGER.debug("Generating line separator section.");

        SolidLine line = new SolidLine();
		line.setColor(new DeviceRgb(SEPARATOR_COLOR_RED, SEPARATOR_COLOR_GREEN, SEPARATOR_COLOR_BLUE));
		return new LineSeparator(line).setMarginRight(10).setMarginLeft(5).setMarginTop(20).setMarginBottom(20);
    }
}
