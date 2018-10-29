package pdfgenerator.sections;

import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerator.exceptions.ImageGenerationException;
import pdfgenerator.models.LocalLandCharge;
import pdfgenerator.services.ImageService;
import pdfgenerator.utility.CellBuilder;
import pdfgenerator.utility.ParagraphBuilder;
import pdfgenerator.utility.Formatters;

import java.io.IOException;

/**
 * Class responsible for generating an LLC section of the PDF.
 */
public final class LlcSection {
    private static final Logger LOGGER = LoggerFactory.getLogger(LlcSection.class);

    private static final Integer MAX_TABLE_COLUMN_WIDTH = 100;

    private static final Integer CHARGE_TABLE_LEFT_COLUMN_WIDTH = 55;
    private static final Integer CHARGE_TABLE_RIGHT_COLUMN_WIDTH = 45;

    private LlcSection() {}

    /**
     * Generates a section of the LLC1 PDF to represent the given Local Land Charge.
     * @param localLandCharge The Local Land Charge to build a PDF section for.
     * @param image An image of the extent for the specified charge.
     * @return A section of the LLC1 PDF to represent the given Local Land Charge.
     * @throws IOException If the font file could not be accessed.
     * @throws ImageGenerationException If the image of the extent could not be generated.
     */
    public static Table generate(LocalLandCharge localLandCharge, Image image) throws IOException, ImageGenerationException {
        LOGGER.debug("Generating local land charge section for: {}", localLandCharge);

        Table chargeTable = new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH)
            }
        )
            .setFixedLayout()
            .setWidth(UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH))

            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Local land charge section")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))

            .addCell(CellBuilder.build(LlcSection.buildChargeTopTable(localLandCharge, image)))
            .addCell(CellBuilder.build(LlcSection.buildChargeBottomTable(localLandCharge)));

        return chargeTable;
    }

    private static Table buildChargeTopTable(LocalLandCharge localLandCharge, Image image) throws ImageGenerationException, IOException {
        image.getAccessibilityProperties().setAlternateDescription("Map showing extent for local land charge " + localLandCharge.getDisplayId());

        String category = localLandCharge.getItem().getChargeType();
        String subCategory = localLandCharge.getItem().getChargeSubCategory();
        if (subCategory != null) {
        	category = category + " - " + subCategory;
        }

        Table chargeDetailsTable = new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH)
            }
        )
            .setFixedLayout()
            .setWidth(UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH))

            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Local land charge details top section")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))
            // HM Land Registry reference
            .addCell(CellBuilder.build(ParagraphBuilder.bold("HM Land Registry reference")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getDisplayId())))

            // Originating authority
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Originating authority")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getOriginatingAuthority())))

            // Authority reference
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Authority reference")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getFurtherInformationReference())))

            // Creation date
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Creation date")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getChargeCreationDate())))

            // Registration date
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Registration date")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getRegistrationDate())))

            // Category
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Category")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(category)))

            // Law
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Law")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getStatutoryProvision())));

        Table chargeExtentTable = new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH)
            }
        )
            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Local land charge extent section")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))

            .addCell(CellBuilder.build(image)
                .add(ParagraphBuilder.normal("Charge extent is outlined on the map"))
                .setPadding(0))
            .setHorizontalAlignment(HorizontalAlignment.RIGHT);

        Table chargeTable = new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(CHARGE_TABLE_LEFT_COLUMN_WIDTH),
                UnitValue.createPercentValue(CHARGE_TABLE_RIGHT_COLUMN_WIDTH)
            }
        )
            .setFixedLayout()
            .setWidth(UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH))

            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Local land charge details column")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))

            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Local land charge extent column")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))

            .addCell(CellBuilder.build(chargeDetailsTable))
            .addCell(CellBuilder.build(chargeExtentTable));

        return chargeTable;
    }

    private static Table buildChargeBottomTable(LocalLandCharge localLandCharge) throws IOException {
        boolean amount = localLandCharge.getItem().getAmountOriginallySecured() != null;
        boolean interestRate = localLandCharge.getItem().getRateOfInterest() != null;
        boolean landSoldDescription = localLandCharge.getItem().getLandSoldDescription() != null;
        boolean landWorksParticulars = localLandCharge.getItem().getLandWorksParticulars() != null;
        boolean landCompensationPaid = localLandCharge.getItem().getLandCompensationPaid() != null;
        boolean amountOfCompensation = localLandCharge.getItem().getAmountOfCompensation() != null;
        boolean landCompensationAmountType = localLandCharge.getItem().getLandCompensationAmountType() != null;
        boolean landCapacityDescription = localLandCharge.getItem().getLandCapacityDescription() != null;

        String category = localLandCharge.getItem().getChargeType();
        String subCategory = localLandCharge.getItem().getChargeSubCategory();
        if (subCategory != null) {
        	category = category + " - " + subCategory;
        }

        String address;
        if(localLandCharge.getItem().getChargeAddress() != null) {
            address = localLandCharge.getItem().getChargeAddress().toAddressString();
        } else if (localLandCharge.getItem().getChargeGeographicDescription() != null) {
            address = localLandCharge.getItem().getChargeGeographicDescription();
        } else {
            address = "";
        }

        Table chargeDetailsTable = new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH)
            }
        )
            .setFixedLayout()
            .setWidth(UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH))

            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Local land charge details bottom section")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))

            // Legal document
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Legal document")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getInstrument())))

            // Location
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Location")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(address)))

            // Description
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Description")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getSupplementaryInformation())));

        // Amount (optional)
        if (amount) {
        	chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Amount")))
                .addCell(CellBuilder.build(ParagraphBuilder.normal(Formatters.formatAmount(localLandCharge.getItem().getAmountOriginallySecured()))));
        }

        // Interest rate (optional)
        if (interestRate) {
        	chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Interest rate")))
                .addCell(CellBuilder.build(ParagraphBuilder.normal(Formatters.formatPercentage(localLandCharge.getItem().getRateOfInterest()))));
        }

        //Land sold (optional)
        if (landSoldDescription) {
            chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Land sold")))
                .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getLandSoldDescription())));
        }

        //Work done (optional)
        if (landWorksParticulars){
            chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Work done")))
                .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getLandWorksParticulars())));
        }

        //Advance payment (optional)
        if (landCompensationPaid) {
            chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Advance payment")))
                .addCell(CellBuilder.build(ParagraphBuilder.normal(Formatters.formatAmount(localLandCharge.getItem().getLandCompensationPaid()))));
        }

        //Total compensation (optional and only in S52 )
        if (localLandCharge.getItem() != null &&
                localLandCharge.getItem().getStatutoryProvision() != null &&
                localLandCharge.getItem().getStatutoryProvision().equals("Land Compensation Act 1973 section 52(8)"))
        {
            String amountOfComp = "Not provided";
            if (amountOfCompensation) {
               amountOfComp = Formatters.formatAmount(localLandCharge.getItem().getAmountOfCompensation());
            }
            chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Total compensation")))
                    .addCell(CellBuilder.build(ParagraphBuilder.normal(amountOfComp)));
        }


        //Agreed or estimated (optional)
        if (landCompensationAmountType) {
            chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Agreed or estimated")))
                .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getLandCompensationAmountType())));
        }
        //Interest in land (optional)
        if (landCapacityDescription){
            chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Interest in land")))
                .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getLandCapacityDescription())));
        }

        // Source information
        chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Source information")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getFurtherInformationLocation())));

        return chargeDetailsTable;
    }

}
