package pdfgenerator.sections;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerator.resources.Images;
import pdfgenerator.utility.CellBuilder;
import pdfgenerator.utility.ParagraphBuilder;

import java.io.IOException;

/**
 * Class responsible for generating the heading section of the PDF.
 */
public final class HeadingSection {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeadingSection.class);

    private static final Integer MAIN_HEADING_FONT_SIZE = 28;
    private static final Integer MAIN_HEADING_FONT_COLOUR_RED = 168;
    private static final Integer MAIN_HEADING_FONT_COLOUR_GREEN = 169;
    private static final Integer MAIN_HEADING_FONT_COLOUR_BLUE = 173;

    private static final Integer SUBHEADING_FONT_SIZE = 24;
    private static final Integer SUBHEADING_FONT_FIXED_LEADING = 40;

    private static final Integer TABLE_LEFT_COLUMN_WIDTH = 90;
    private static final Integer TABLE_RIGHT_COLUMN_WIDTH = 10;

    private HeadingSection() {}

    /**
     * Builds and returns the Search Section of the LLC1 PDF.
     * @return The Search Section of the LLC1 PDF.
     * @throws IOException If the font file could not be accessed.
     */
    public static Table generate() throws IOException {
        LOGGER.debug("Generating heading section.");

        return new Table(new UnitValue[]{
                UnitValue.createPercentValue(TABLE_LEFT_COLUMN_WIDTH),
                UnitValue.createPercentValue(TABLE_RIGHT_COLUMN_WIDTH)
            })

            .addHeaderCell(CellBuilder.build(
                    ParagraphBuilder.normal("Organisation")
                            .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                            .setFontSize(1)))

            .addHeaderCell(CellBuilder.build(
                    ParagraphBuilder.normal("Organisation icon")
                            .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                            .setFontSize(1)))

            .addCell(CellBuilder.build(
                ParagraphBuilder.geoheading("HM Land Registry")
                    .setFontSize(MAIN_HEADING_FONT_SIZE)
                    .setFontColor(
                        new DeviceRgb(MAIN_HEADING_FONT_COLOUR_RED, MAIN_HEADING_FONT_COLOUR_GREEN, MAIN_HEADING_FONT_COLOUR_BLUE)
                    ),

                ParagraphBuilder.heading("Local land charges official search")
                    .setFontSize(SUBHEADING_FONT_SIZE)
                    .setFixedLeading(SUBHEADING_FONT_FIXED_LEADING)
            ))

            .addCell(CellBuilder.build(Images.getInstance().getLogo()
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT)));
    }
}
