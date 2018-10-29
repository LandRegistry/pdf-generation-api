package pdfgenerator.sections;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerationapi.models.Llc1PdfRequest;
import pdfgenerator.exceptions.ImageGenerationException;
import pdfgenerator.services.ImageService;
import pdfgenerator.utility.CellBuilder;
import pdfgenerator.utility.ParagraphBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Class responsible for generating the search section of the PDF.
 */
public final class SearchSection {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchSection.class);

    private static final String DATE_FORMAT_PATTERN = "HH:mm:ss 'on' d MMMM uuuu";

    private static final Integer MAX_TABLE_COLUMN_WIDTH = 100;

    private static final Integer DISCLAIMER_FONT_SIZE = 12;
    private static final float DISCLAIMER_PADDING_TOP = 2;
    private static final float DISCLAIMER_PADDING_BOTTOM = 2;

    private static final Integer TABLE_BORDER_RED = 224;
    private static final Integer TABLE_BORDER_GREEN = 224;
    private static final Integer TABLE_BORDER_BLUE = 224;
    private static final Integer TABLE_BORDER_WIDTH = 2;

    private static final Integer SEARCH_TABLE_LEFT_COLUMN_WIDTH = 55;
    private static final Integer SEARCH_TABLE_RIGHT_COLUMN_WIDTH = 45;

    private static final Integer SEARCH_TABLE_NOTE_FONT_SIZE = 9;

    private static final Integer TABLE_PADDING_LEFT = 5;
    private static final Integer TABLE_PADDING_RIGHT = 5;

    private static final String SYSTEM_TIMEZONE = "UTC";
    private static final String DISPLAY_TIMEZONE = "Europe/London";

    private SearchSection() {}

    /**
     * @param llc1PdfRequest The input to the llc1 end point.
     * @return A section of the LLC1 PDF to represent search the user made.
     * @throws IOException If the font file could not be accessed.
     * @throws ImageGenerationException If the image of the extent could not be generated.
     */
    public static Table generate(Llc1PdfRequest llc1PdfRequest) throws IOException, ImageGenerationException {
        Table searchSection = SearchSection.generateSearchTable(llc1PdfRequest);

        SolidBorder border = new SolidBorder(
            new DeviceRgb(TABLE_BORDER_RED, TABLE_BORDER_GREEN, TABLE_BORDER_BLUE),
            TABLE_BORDER_WIDTH
        );

        Table combinedSection = new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH)
            })

            .setFixedLayout()
            .setWidth(UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH))

            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Search extent section")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))

            .addCell(CellBuilder.build(
                ParagraphBuilder.normal("It is hereby certified that the search of land and property as shown below reveals registrations up to and including the date and time of this certificate"))
                    .setPaddings(DISCLAIMER_PADDING_TOP,TABLE_PADDING_RIGHT, DISCLAIMER_PADDING_BOTTOM, TABLE_PADDING_LEFT)
                    .setFontSize(DISCLAIMER_FONT_SIZE)
            )
            .addCell(CellBuilder.build(searchSection))
            .setBorder(border);

        return combinedSection;
    }

    private static Table generateSearchTable(Llc1PdfRequest llc1PdfRequest) throws IOException, ImageGenerationException {
        LOGGER.debug("Generating search section.");

        String producedAtValue = LocalDateTime.now().atZone(ZoneId.of(SYSTEM_TIMEZONE))
        		.withZoneSameInstant(ZoneId.of(DISPLAY_TIMEZONE))
        		.format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));

        // Search extent image:
        Image searchExtentImage = ImageService.generate(llc1PdfRequest.getExtents().getFeatures());
        searchExtentImage.getAccessibilityProperties().setAlternateDescription("Map showing searched area");

        String referenceNumber = String.format("%09d", llc1PdfRequest.getReferenceNumber())
                .replaceAll("(.{3})(?!$)", "$1 ");

        Table searchDetailsTable = new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH)
            })

            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Search details inner column")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))

            // Certificate produced at row:
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Time and date")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(producedAtValue)))

            // Certificate reference row:
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Reference")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(referenceNumber)))

            // Land and property row:
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Description of search area")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(llc1PdfRequest.getDescription())));

        Table searchExtentTable = new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH)
            })

            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Search extent inner column")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))

            .addCell(CellBuilder.build(searchExtentImage)
                .add(ParagraphBuilder.normal("Search area is outlined on the map"))
                .setFontSize(SEARCH_TABLE_NOTE_FONT_SIZE)
            )
            .setHorizontalAlignment(HorizontalAlignment.RIGHT);

        return new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(SEARCH_TABLE_LEFT_COLUMN_WIDTH),
                UnitValue.createPercentValue(SEARCH_TABLE_RIGHT_COLUMN_WIDTH)
            })

            .setFixedLayout()
            .setWidth(UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH))

            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Search details outer column")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))

            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Search extent outer column")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))

            .addCell(CellBuilder.build(searchDetailsTable)
                .setPaddingLeft(TABLE_PADDING_LEFT)
                .setPaddingRight(TABLE_PADDING_RIGHT))
            .addCell(CellBuilder.build(searchExtentTable));
    }
}
