package pdfgenerator.sections;

import com.itextpdf.layout.element.Cell;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class responsible for generating an LON section of the PDF.
 */
public final class LonSection {
    private static final Logger LOGGER = LoggerFactory.getLogger(LonSection.class);

    private static final Integer MAX_TABLE_COLUMN_WIDTH = 100;

    private static final Integer CHARGE_TABLE_LEFT_COLUMN_WIDTH = 55;
    private static final Integer CHARGE_TABLE_RIGHT_COLUMN_WIDTH = 45;

    private static final Map<String, String> DOC_TYPE_TEXT = new LinkedHashMap<String, String>();
    static {
    	DOC_TYPE_TEXT.put("temporary-certificate", "Temporary certificate");
    	DOC_TYPE_TEXT.put("definitive-certificate", "Definitive certificate");
    	DOC_TYPE_TEXT.put("form-a", "Form A and colour plan");
    	DOC_TYPE_TEXT.put("form-b", "Form B");
    	DOC_TYPE_TEXT.put("court-order", "Court order");
    }
    
    private LonSection() {}

    /**
     * Generates a section of the LLC1 PDF to represent the given Local Land Charge of type LON.
     * @param localLandCharge The Local Land Charge to build a PDF section for.
     * @param image An image of the extent for the specified charge.
     * @return A section of the LLC1 PDF to represent the given Local Land Charge of type LON.
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

            .addCell(CellBuilder.build(LonSection.buildChargeTopTable(localLandCharge, image)))
            .addCell(CellBuilder.build(LonSection.buildChargeBottomTable(localLandCharge)));

        return chargeTable;
    }

    private static Table buildChargeTopTable(LocalLandCharge localLandCharge, Image image) throws IOException, ImageGenerationException {
        image.getAccessibilityProperties().setAlternateDescription("Map showing extent for local land charge " + localLandCharge.getDisplayId());

        String address;
        if(localLandCharge.getItem().getChargeAddress() != null) {
            address = localLandCharge.getItem().getChargeAddress().toAddressString();
        } else if (localLandCharge.getItem().getChargeGeographicDescription() != null) {
            address = localLandCharge.getItem().getChargeGeographicDescription();
        } else {
            address = "";
        }

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
            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Local land charge details top section")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))
            // HM Land Registry reference
            .addCell(CellBuilder.build(ParagraphBuilder.bold("HM Land Registry reference")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getDisplayId())))

            // Registration date
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Registration date")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getRegistrationDate())))

            // Category
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Category")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(category)))

            // Law
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Law")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getStatutoryProvision())))

            // Legal document
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Legal document")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getInstrument())))

            // Location (dominant building)
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Location (dominant building)")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(address)));

        Table chargeExtentTable = new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(100)
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
        String category = localLandCharge.getItem().getChargeType();
        String subCategory = localLandCharge.getItem().getChargeSubCategory();
        if (subCategory != null) {
        	category = category + " - " + subCategory;
        }

        ArrayList<String> availableDocs = new ArrayList<String>();
		Set<String> chargeDocTypes = localLandCharge.getItem().getDocumentsFiled().keySet();
		for (String docType : DOC_TYPE_TEXT.keySet()) {
			if (chargeDocTypes.contains(docType)) {
				availableDocs.add(DOC_TYPE_TEXT.get(docType));
			}
		}

        boolean tempDate = localLandCharge.getItem().getTribunalTemporaryCertificateDate() != null;
        boolean tempExpiry = localLandCharge.getItem().getTribunalTemporaryCertificateExpiryDate() != null;
        boolean defDate = localLandCharge.getItem().getTribunalDefinitiveCertificateDate() != null;
        boolean defExpiry = localLandCharge.getItem().getExpiryDate() != null;

        Table chargeDetailsTable = new Table(
            new UnitValue[] {
                UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH)
            }
        )
            .setFixedLayout()
            .setWidth(UnitValue.createPercentValue(MAX_TABLE_COLUMN_WIDTH))

            .addHeaderCell(CellBuilder.build(
                ParagraphBuilder.normal("Local land charge details inner column")
                    .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE)
                    .setFontSize(1)))

            // Available Documents
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Available documents")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(String.join(System.lineSeparator(), availableDocs))));

        if (availableDocs.contains("Temporary certificate")) {
	        if (tempDate) {
	            // Date (temporary certificate)
	            chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Start date (temporary certificate)")))
	                    .addCell(CellBuilder.build(ParagraphBuilder.normal(
	                            localLandCharge.getItem().getTribunalTemporaryCertificateDate())));
	        } else {
	        	chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Start date (temporary certificate)")))
	            		.addCell(CellBuilder.build(ParagraphBuilder.normal("Not provided")));
	        }
	
	        if (tempExpiry) {
	            // Expiry (temporary certificate)
	            chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Expiry date (temporary certificate)")))
	                    .addCell(CellBuilder.build(ParagraphBuilder.normal(
	                            localLandCharge.getItem().getTribunalTemporaryCertificateExpiryDate())));
	        } else {
	        	chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Expiry date (temporary certificate)")))
			            .addCell(CellBuilder.build(ParagraphBuilder.normal("Not provided")));
	        }
        }

        if (availableDocs.contains("Definitive certificate")) {
	        if (defDate) {
	            // Date (definitive certificate)
	            chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Start date (definitive certificate)")))
	                    .addCell(CellBuilder.build(ParagraphBuilder.normal(
	                            localLandCharge.getItem().getTribunalDefinitiveCertificateDate())));
	        } else {
	        	chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Start date (definitive certificate)")))
	            		.addCell(CellBuilder.build(ParagraphBuilder.normal("Not provided")));
	        }
	
	        if (defExpiry) {
	            // Date (definitive certificate)
	            chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Expiry date (definitive certificate)")))
	                    .addCell(CellBuilder.build(ParagraphBuilder.normal(
	                            localLandCharge.getItem().getExpiryDate())));
	        } else {
	        	chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Expiry date (definitive certificate)")))
	            		.addCell(CellBuilder.build(ParagraphBuilder.normal("Not provided")));
	        }
        }

        // Applicant Name
        String applicantName = "Not provided";
        if (localLandCharge.getItem().getApplicantName() != null 
        		&& !localLandCharge.getItem().getApplicantName().trim().equals("")) {
        	applicantName = localLandCharge.getItem().getApplicantName();
        }
        chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Applicant name")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(applicantName)));

        // Applicant Address
        String applicantAddress = "Not provided";
        if (localLandCharge.getItem().getApplicantAddress() != null) {
        	applicantAddress = localLandCharge.getItem().getApplicantAddress().toAddressString();
        }
        chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Applicant address")))
        	.addCell(CellBuilder.build(ParagraphBuilder.normal(applicantAddress)));

        // Interest in land
        String servientLandInterestDescription = "Not provided";
        if (localLandCharge.getItem().getServientLandInterestDescription() != null 
        		&& !localLandCharge.getItem().getServientLandInterestDescription().trim().equals("")) {
        	servientLandInterestDescription = localLandCharge.getItem().getServientLandInterestDescription();
        }
        chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Interest in land")))
        	.addCell(CellBuilder.build(ParagraphBuilder.normal(servientLandInterestDescription)));

        // Height of servient land development
        String structureHeight = "Not provided";
        if (localLandCharge.getItem().getStructurePositionAndDimension().getHeight() != null 
        		&& !localLandCharge.getItem().getStructurePositionAndDimension().getHeight().trim().equals("")) {
        	structureHeight = localLandCharge.getItem().getStructurePositionAndDimension().toHeightString();
        }
        chargeDetailsTable.addCell(CellBuilder.build(ParagraphBuilder.bold("Height of servient land development")))
        	.addCell(CellBuilder.build(ParagraphBuilder.normal(structureHeight)))

            // Structure Position
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Covers all or part of the extent")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(
                    localLandCharge.getItem().getStructurePositionAndDimension().toPositionString())))

            // Source information
            .addCell(CellBuilder.build(ParagraphBuilder.bold("Source information")))
            .addCell(CellBuilder.build(ParagraphBuilder.normal(localLandCharge.getItem().getFurtherInformationLocation())));

        return chargeDetailsTable;
    }
}
