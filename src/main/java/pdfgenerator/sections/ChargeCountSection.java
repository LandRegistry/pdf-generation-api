package pdfgenerator.sections;

import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.UnitValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerator.resources.Images;
import pdfgenerator.utility.CellBuilder;
import pdfgenerator.utility.ParagraphBuilder;

import java.io.IOException;

/**
 * Class responsible for generating the charge count section of the PDF.
 */
public final class ChargeCountSection {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeCountSection.class);

    private static final Integer NUMBER_OF_CHARGES_FONT_SIZE = 16;
    private static final float PADDING_TOP = 10;
    private static final float PADDING_BOTTOM = 15;

    private ChargeCountSection() {}

    /**
     * Builds and returns the charge count section of the LLC1 PDF.
     * @return The charge count section of the LLC1 PDF.
     * @throws IOException If the font file could not be accessed.
     */
    public static Table generate(int numberOfLLC) throws IOException {
        LOGGER.debug("Generating charge count section.");

        String chargeCountText = null;
        if(numberOfLLC == 1) {
            chargeCountText = "There is " + numberOfLLC + " local land charge in this area";
        } else {
            chargeCountText = "There are " + numberOfLLC + " local land charges in this area";
        }

        // Number of Charges Container:
        return new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(100)
            })
            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Number of charges section")
                        .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                        .setFontSize(1)))

            .addCell(CellBuilder.build(
                ParagraphBuilder.bold(chargeCountText)
                    .setFontSize(NUMBER_OF_CHARGES_FONT_SIZE)
                )
                .setPaddingTop(PADDING_TOP)
                .setPaddingBottom(PADDING_BOTTOM)
            );
    }
}
