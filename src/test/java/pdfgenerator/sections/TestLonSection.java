package pdfgenerator.sections;

import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.layout.element.*;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.TestUtils;
import pdfgenerator.exceptions.ImageGenerationException;
import pdfgenerator.models.Address;
import pdfgenerator.models.LocalLandCharge;
import pdfgenerator.models.StructurePositionDimension;
import pdfgenerator.services.ImageService;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageService.class})
public class TestLonSection extends EasyMockSupport {

    @Before
    public void initialize() {
        PowerMock.mockStatic(ImageService.class);
    }

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
    public void testGenerate() throws IOException, ImageGenerationException {
        LocalLandCharge lon = TestUtils.buildLONCharge();
        String chargeAddress = lon.getItem().getChargeAddress().toAddressString();

        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);
        EasyMock.expect(properties.setAlternateDescription("Map showing extent for local land charge abc")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);
        replayAll();

        Table table = LonSection.generate(lon, image);
        Table chargeTopSection = (Table)table.getCell(0, 0).getChildren().get(0);
        Table chargeDetailsTable = (Table)chargeTopSection.getCell(0,0).getChildren().get(0);
        Table chargeExtentTable = (Table)chargeTopSection.getCell(0,1).getChildren().get(0);
        Table chargeBottomSection = (Table)table.getCell(1, 0).getChildren().get(0);

        String category = lon.getItem().getChargeType();
        String subCategory = lon.getItem().getChargeSubCategory();
        if (subCategory != null) {
        	category = category + " - " + subCategory;
        }
        int rowCount = 0;
        String landRegistryReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landRegistryReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String legalDocumentHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String legalDocumentContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationDominantBuildingHeader= ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationDominantBuildingContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();

        rowCount = 0;
        String availableDocumentsHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String availableDocumentsContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String startDateTempCertHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String startDateTempCertContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String expiryDateTempCertHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String expiryDateTempCertContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String startDateDefinitiveCertHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String startDateDefinitiveCertContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String expiryDateDefinitiveCertHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String expiryDateDefinitiveCertContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String applicantNameHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String applicantNameContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String applicantAddressHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String applicantAddressContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++, 0).getChildren().get(0)).getChildren().get(0)).getText();
        String interestInLandHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String interestInLandContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String servientLandHeightHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String servientLandHeightContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String coversAllOrPartOfExtentHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String coversAllOrPartOfExtentContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String sourceInformationHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String sourceInformationContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();

        String chargeExtentText = ((Text)((Paragraph)chargeExtentTable.getCell(0,0).getChildren().get(1)).getChildren().get(0)).getText();

        assertThat(table, is(not(nullValue())));
        assertThat(landRegistryReferenceHeader, is("HM Land Registry reference"));
        assertThat(landRegistryReferenceContent, is(lon.getDisplayId()));
        assertThat(registrationDateHeader, is("Registration date"));
        assertThat(registrationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(lon.getItem().getRegistrationDate())));
        assertThat(categoryHeader, is("Category"));
        assertThat(categoryContent, is(category));
        assertThat(lawHeader, is("Law"));
        assertThat(lawContent, is(lon.getItem().getStatutoryProvision()));
        assertThat(locationDominantBuildingHeader, is("Location (dominant building)"));
        assertThat(locationDominantBuildingContent, is(chargeAddress));
        assertThat(legalDocumentHeader, is("Legal document"));
        assertThat(legalDocumentContent, is(lon.getItem().getInstrument()));
        assertThat(availableDocumentsHeader, is("Available documents"));
        assertThat(availableDocumentsContent, is("Temporary certificate" + System.lineSeparator() + "Definitive certificate" + System.lineSeparator() +
                "Form A and colour plan" + System.lineSeparator() + "Form B" + System.lineSeparator() + "Court order"));
        assertThat(startDateTempCertHeader, is("Start date (temporary certificate)"));
        assertThat(startDateTempCertContent, is(new SimpleDateFormat("d MMMM yyyy").format(lon.getItem().getTribunalTemporaryCertificateDate())));
        assertThat(expiryDateTempCertHeader, is("Expiry date (temporary certificate)"));
        assertThat(expiryDateTempCertContent, is(new SimpleDateFormat("d MMMM yyyy").format(lon.getItem().getTribunalTemporaryCertificateExpiryDate())));
        assertThat(startDateDefinitiveCertHeader, is("Start date (definitive certificate)"));
        assertThat(startDateDefinitiveCertContent, is(new SimpleDateFormat("d MMMM yyyy").format(lon.getItem().getTribunalDefinitiveCertificateDate())));
        assertThat(expiryDateDefinitiveCertHeader, is("Expiry date (definitive certificate)"));
        assertThat(expiryDateDefinitiveCertContent, is(new SimpleDateFormat("d MMMM yyyy").format(lon.getItem().getExpiryDate())));
        assertThat(applicantNameHeader, is("Applicant name"));
        assertThat(applicantNameContent, is(lon.getItem().getApplicantName()));
        assertThat(applicantAddressHeader, is("Applicant address"));
        assertThat(applicantAddressContent, is(lon.getItem().getApplicantAddress().toAddressString()));
        assertThat(interestInLandHeader, is("Interest in land"));
        assertThat(interestInLandContent, is(lon.getItem().getServientLandInterestDescription()));
        assertThat(servientLandHeightHeader, is("Height of servient land development"));
        assertThat(servientLandHeightContent, is("Unlimited height"));
        assertThat(coversAllOrPartOfExtentHeader, is("Covers all or part of the extent"));
        assertThat(coversAllOrPartOfExtentContent, is("All of the extent"));
        assertThat(sourceInformationHeader, is("Source information"));
        assertThat(sourceInformationContent, is(lon.getItem().getFurtherInformationLocation()));

        assertThat(chargeExtentText, is("Charge extent is outlined on the map"));

        verifyAll();
    }

    @Test
    public void testGenerateBulkLoadWithoutOptionalFields() throws IOException, ImageGenerationException {
        LocalLandCharge lon = TestUtils.buildLONCharge();
        lon.getItem().setApplicantName(null);
        lon.getItem().setApplicantAddress(null);
        lon.getItem().setServientLandInterestDescription(null);
        lon.getItem().getStructurePositionAndDimension().setHeight(null);
        lon.getItem().getStructurePositionAndDimension().setUnits(null);
        lon.getItem().setTribunalDefinitiveCertificateDate(null);
        
        String chargeAddress = lon.getItem().getChargeAddress().toAddressString();

        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);
        EasyMock.expect(properties.setAlternateDescription("Map showing extent for local land charge abc")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);
        replayAll();

        Table table = LonSection.generate(lon, image);
        Table chargeTopSection = (Table)table.getCell(0, 0).getChildren().get(0);
        Table chargeDetailsTable = (Table)chargeTopSection.getCell(0,0).getChildren().get(0);
        Table chargeExtentTable = (Table)chargeTopSection.getCell(0,1).getChildren().get(0);
        Table chargeBottomSection = (Table)table.getCell(1, 0).getChildren().get(0);

        String category = lon.getItem().getChargeType();
        String subCategory = lon.getItem().getChargeSubCategory();
        if (subCategory != null) {
        	category = category + " - " + subCategory;
        }

        int rowCount = 0;
        String landRegistryReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landRegistryReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String legalDocumentHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String legalDocumentContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationDominantBuildingHeader= ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationDominantBuildingContent = ((Text)((Paragraph)chargeDetailsTable.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();

        rowCount = 0;
        String availableDocumentsHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String availableDocumentsContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String startDateTempCertHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String startDateTempCertContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String expiryDateTempCertHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String expiryDateTempCertContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String startDateDefinitiveCertHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String startDateDefinitiveCertContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String expiryDateDefinitiveCertHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String expiryDateDefinitiveCertContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String applicantNameHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String applicantNameContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String applicantAddressHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String applicantAddressContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++, 0).getChildren().get(0)).getChildren().get(0)).getText();
        String interestInLandHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String interestInLandContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String servientLandHeightHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String servientLandHeightContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String coversAllOrPartOfExtentHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String coversAllOrPartOfExtentContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String sourceInformationHeader = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();
        String sourceInformationContent = ((Text)((Paragraph)chargeBottomSection.getCell(rowCount++,0).getChildren().get(0)).getChildren().get(0)).getText();

        String chargeExtentText = ((Text)((Paragraph)chargeExtentTable.getCell(0,0).getChildren().get(1)).getChildren().get(0)).getText();

        assertThat(table, is(not(nullValue())));
        assertThat(landRegistryReferenceHeader, is("HM Land Registry reference"));
        assertThat(landRegistryReferenceContent, is(lon.getDisplayId()));
        assertThat(registrationDateHeader, is("Registration date"));
        assertThat(registrationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(lon.getItem().getRegistrationDate())));
        assertThat(categoryHeader, is("Category"));
        assertThat(categoryContent, is(category));
        assertThat(lawHeader, is("Law"));
        assertThat(lawContent, is(lon.getItem().getStatutoryProvision()));
        assertThat(locationDominantBuildingHeader, is("Location (dominant building)"));
        assertThat(locationDominantBuildingContent, is(chargeAddress));
        assertThat(legalDocumentHeader, is("Legal document"));
        assertThat(legalDocumentContent, is(lon.getItem().getInstrument()));
        assertThat(availableDocumentsHeader, is("Available documents"));
        assertThat(availableDocumentsContent, is("Temporary certificate" + System.lineSeparator() + "Definitive certificate" + System.lineSeparator() +
                "Form A and colour plan" + System.lineSeparator() + "Form B" + System.lineSeparator() + "Court order"));
        assertThat(startDateTempCertHeader, is("Start date (temporary certificate)"));
        assertThat(startDateTempCertContent, is(new SimpleDateFormat("d MMMM yyyy").format(lon.getItem().getTribunalTemporaryCertificateDate())));
        assertThat(expiryDateTempCertHeader, is("Expiry date (temporary certificate)"));
        assertThat(expiryDateTempCertContent, is(new SimpleDateFormat("d MMMM yyyy").format(lon.getItem().getTribunalTemporaryCertificateExpiryDate())));
        assertThat(startDateDefinitiveCertHeader, is("Start date (definitive certificate)"));
        assertThat(startDateDefinitiveCertContent, is("Not provided"));
        assertThat(expiryDateDefinitiveCertHeader, is("Expiry date (definitive certificate)"));
        assertThat(expiryDateDefinitiveCertContent, is(new SimpleDateFormat("d MMMM yyyy").format(lon.getItem().getExpiryDate())));
        assertThat(applicantNameHeader, is("Applicant name"));
        assertThat(applicantNameContent, is("Not provided"));
        assertThat(applicantAddressHeader, is("Applicant address"));
        assertThat(applicantAddressContent, is("Not provided"));
        assertThat(interestInLandHeader, is("Interest in land"));
        assertThat(interestInLandContent, is("Not provided"));
        assertThat(servientLandHeightHeader, is("Height of servient land development"));
        assertThat(servientLandHeightContent, is("Not provided"));
        assertThat(coversAllOrPartOfExtentHeader, is("Covers all or part of the extent"));
        assertThat(coversAllOrPartOfExtentContent, is("All of the extent"));
        assertThat(sourceInformationHeader, is("Source information"));
        assertThat(sourceInformationContent, is(lon.getItem().getFurtherInformationLocation()));

        assertThat(chargeExtentText, is("Charge extent is outlined on the map"));

        verifyAll();
    }

    @Test
    public void testFormattingAndHeaders() throws IOException, ImageGenerationException {
        LocalLandCharge lon = TestUtils.buildLONCharge();

        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);
        EasyMock.expect(properties.setAlternateDescription("Map showing extent for local land charge abc")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);
        EasyMock.expect(ImageService.generate(lon.getGeometry().getFeatures())).andReturn(image);
        replayAll();

        Table table = LlcSection.generate(lon, image);
        Table chargeTopSection = (Table)table.getCell(0, 0).getChildren().get(0);
        Table chargeDetailsTable = (Table)chargeTopSection.getCell(0,0).getChildren().get(0);
        Table chargeExtentTable = (Table)chargeTopSection.getCell(0,1).getChildren().get(0);
        Table chargeBottomSection = (Table)table.getCell(1, 0).getChildren().get(0);

        String tableColumnHeader = ((Text)((Paragraph)((Cell)table.getHeader().getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).getText();
        String chargeTopSectionLeftColumnHeader = ((Text)((Paragraph)((Cell)chargeTopSection.getHeader().getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).getText();
        String chargeTopSectionRightColumnHeader = ((Text)((Paragraph)((Cell)chargeTopSection.getHeader().getChildren().get(1)).getChildren().get(0)).getChildren().get(0)).getText();
        String chargeDetailsTableHeader = ((Text)((Paragraph)((Cell)chargeDetailsTable.getHeader().getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).getText();
        String chargeExtentTableHeader = ((Text)((Paragraph)((Cell)chargeExtentTable.getHeader().getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).getText();
        String chargeBottomSectionHeader = ((Text)((Paragraph)((Cell)chargeBottomSection.getHeader().getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).getText();

        assertThat(table, is(not(nullValue())));
        assertThat(chargeTopSection, is(not(nullValue())));
        assertThat(chargeDetailsTable, is(not(nullValue())));
        assertThat(chargeExtentTable, is(not(nullValue())));
        assertThat(chargeBottomSection, is(not(nullValue())));

        assertThat(table.getColumnWidth(0).getValue(), is(100f));
        assertThat(chargeTopSection.getColumnWidth(0).getValue(), is(55f));
        assertThat(chargeTopSection.getColumnWidth(1).getValue(), is(45f));
        assertThat(chargeDetailsTable.getColumnWidth(0).getValue(), is(100f));
        assertThat(chargeExtentTable.getColumnWidth(0).getValue(), is(100f));
        assertThat(chargeExtentTable.getColumnWidth(0).getValue(), is(100f));
        assertThat(chargeExtentTable.getColumnWidth(0).getValue(), is(100f));

        assertThat(tableColumnHeader, is("Local land charge section"));
        assertThat(chargeTopSectionLeftColumnHeader, is("Local land charge details column"));
        assertThat(chargeTopSectionRightColumnHeader, is("Local land charge extent column"));
        assertThat(chargeDetailsTableHeader, is("Local land charge details top section"));
        assertThat(chargeExtentTableHeader, is("Local land charge extent section"));
        assertThat(chargeBottomSectionHeader, is("Local land charge details bottom section"));
    }
}