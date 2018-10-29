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
import pdfgenerator.models.LocalLandCharge;
import pdfgenerator.services.ImageService;
import pdfgenerator.utility.Formatters;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageService.class})
public class TestLlcSection extends EasyMockSupport {

    @Before
    public void initialize() throws Exception {
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
    public void testFormattingAndHeaders() throws IOException, ImageGenerationException {
        LocalLandCharge localLandCharge = TestUtils.buildLocalLandCharge();
        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);
        EasyMock.expect(properties.setAlternateDescription("Map showing extent for local land charge abc")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);
        replayAll();

        Table table = LlcSection.generate(localLandCharge, image);
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

    @Test
    public void testGenerate() throws IOException, ImageGenerationException {
        LocalLandCharge localLandCharge = TestUtils.buildLocalLandCharge();
        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);

        EasyMock.expect(properties.setAlternateDescription("Map showing extent for local land charge abc")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);
        replayAll();

        Table table = LlcSection.generate(localLandCharge, image);
        Table chargeTopSection = (Table)table.getCell(0, 0).getChildren().get(0);
        Table chargeDetailsTable = (Table)chargeTopSection.getCell(0,0).getChildren().get(0);
        Table chargeExtentTable = (Table)chargeTopSection.getCell(0,1).getChildren().get(0);
        Table chargeBottomSection = (Table)table.getCell(1, 0).getChildren().get(0);

        String category = localLandCharge.getItem().getChargeType();
        String subCategory = localLandCharge.getItem().getChargeSubCategory();
        if (subCategory != null) {
        	category = category + " - " + subCategory;
        }
        
        String landRegistryReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landRegistryReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(1,0).getChildren().get(0)).getChildren().get(0)).getText();
        String originatingAuthorityHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(2,0).getChildren().get(0)).getChildren().get(0)).getText();
        String originatingAuthorityContent = ((Text)((Paragraph)chargeDetailsTable.getCell(3,0).getChildren().get(0)).getChildren().get(0)).getText();
        String authorityReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(4,0).getChildren().get(0)).getChildren().get(0)).getText();
        String authorityReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(5,0).getChildren().get(0)).getChildren().get(0)).getText();
        String creationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(6,0).getChildren().get(0)).getChildren().get(0)).getText();
        String creationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(7,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(8,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(9,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(10,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryContent = ((Text)((Paragraph)chargeDetailsTable.getCell(11,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(12,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawContent = ((Text)((Paragraph)chargeDetailsTable.getCell(13,0).getChildren().get(0)).getChildren().get(0)).getText();

        String legalDocumentHeader = ((Text)((Paragraph)chargeBottomSection.getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String legalDocumentContent = ((Text)((Paragraph)chargeBottomSection.getCell(1,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationHeader = ((Text)((Paragraph)chargeBottomSection.getCell(2,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationContent = ((Text)((Paragraph)chargeBottomSection.getCell(3,0).getChildren().get(0)).getChildren().get(0)).getText();
        String descriptionHeader = ((Text)((Paragraph)chargeBottomSection.getCell(4,0).getChildren().get(0)).getChildren().get(0)).getText();
        String descriptionContent = ((Text)((Paragraph)chargeBottomSection.getCell(5,0).getChildren().get(0)).getChildren().get(0)).getText();
        String sourceInformationHeader = ((Text)((Paragraph)chargeBottomSection.getCell(6,0).getChildren().get(0)).getChildren().get(0)).getText();
        String sourceInformationContent = ((Text)((Paragraph)chargeBottomSection.getCell(7,0).getChildren().get(0)).getChildren().get(0)).getText();

        String chargeExtentText = ((Text)((Paragraph)chargeExtentTable.getCell(0,0).getChildren().get(1)).getChildren().get(0)).getText();

        assertThat(landRegistryReferenceHeader, is("HM Land Registry reference"));
        assertThat(landRegistryReferenceContent, is(localLandCharge.getDisplayId()));
        assertThat(originatingAuthorityHeader, is("Originating authority"));
        assertThat(originatingAuthorityContent, is(localLandCharge.getItem().getOriginatingAuthority()));
        assertThat(authorityReferenceHeader, is("Authority reference"));
        assertThat(authorityReferenceContent, is(localLandCharge.getItem().getFurtherInformationReference()));
        assertThat(creationDateHeader, is("Creation date"));
        assertThat(creationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(localLandCharge.getItem().getChargeCreationDate())));
        assertThat(registrationDateHeader, is("Registration date"));
        assertThat(registrationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(localLandCharge.getItem().getRegistrationDate())));
        assertThat(categoryHeader, is("Category"));
        assertThat(categoryContent, is(category));
        assertThat(lawHeader, is("Law"));
        assertThat(lawContent, is(localLandCharge.getItem().getStatutoryProvision()));
        assertThat(legalDocumentHeader, is("Legal document"));
        assertThat(legalDocumentContent, is(localLandCharge.getItem().getInstrument()));
        assertThat(locationHeader, is("Location"));
        assertThat(locationContent, is(localLandCharge.getItem().getChargeGeographicDescription()));
        assertThat(descriptionHeader, is("Description"));
        assertThat(descriptionContent, is(localLandCharge.getItem().getSupplementaryInformation()));
        assertThat(sourceInformationHeader, is("Source information"));
        assertThat(sourceInformationContent, is(localLandCharge.getItem().getFurtherInformationLocation()));

        assertThat(chargeExtentText, is("Charge extent is outlined on the map"));

        verifyAll();
    }

    @Test
    public void testGenerateSpecificFinancial() throws IOException, ImageGenerationException {
        LocalLandCharge localLandCharge = TestUtils.buildLocalLandCharge();
        localLandCharge.getItem().setAmountOriginallySecured("1234.56");
        localLandCharge.getItem().setRateOfInterest("1234.56");
        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);
        EasyMock.expect(properties.setAlternateDescription("Map showing extent for local land charge abc")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);
        replayAll();

        Table table = LlcSection.generate(localLandCharge, image);
        Table chargeTopSection = (Table)table.getCell(0, 0).getChildren().get(0);
        Table chargeDetailsTable = (Table)chargeTopSection.getCell(0,0).getChildren().get(0);
        Table chargeExtentTable = (Table)chargeTopSection.getCell(0,1).getChildren().get(0);
        Table chargeBottomSection = (Table)table.getCell(1, 0).getChildren().get(0);

        String category = localLandCharge.getItem().getChargeType();
        String subCategory = localLandCharge.getItem().getChargeSubCategory();
        if (subCategory != null) {
        	category = category + " - " + subCategory;
        }

        String landRegistryReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landRegistryReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(1,0).getChildren().get(0)).getChildren().get(0)).getText();
        String originatingAuthorityHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(2,0).getChildren().get(0)).getChildren().get(0)).getText();
        String originatingAuthorityContent = ((Text)((Paragraph)chargeDetailsTable.getCell(3,0).getChildren().get(0)).getChildren().get(0)).getText();
        String authorityReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(4,0).getChildren().get(0)).getChildren().get(0)).getText();
        String authorityReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(5,0).getChildren().get(0)).getChildren().get(0)).getText();
        String creationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(6,0).getChildren().get(0)).getChildren().get(0)).getText();
        String creationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(7,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(8,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(9,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(10,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryContent = ((Text)((Paragraph)chargeDetailsTable.getCell(11,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(12,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawContent = ((Text)((Paragraph)chargeDetailsTable.getCell(13,0).getChildren().get(0)).getChildren().get(0)).getText();

        String legalDocumentHeader = ((Text)((Paragraph)chargeBottomSection.getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String legalDocumentContent = ((Text)((Paragraph)chargeBottomSection.getCell(1,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationHeader = ((Text)((Paragraph)chargeBottomSection.getCell(2,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationContent = ((Text)((Paragraph)chargeBottomSection.getCell(3,0).getChildren().get(0)).getChildren().get(0)).getText();
        String descriptionHeader = ((Text)((Paragraph)chargeBottomSection.getCell(4,0).getChildren().get(0)).getChildren().get(0)).getText();
        String descriptionContent = ((Text)((Paragraph)chargeBottomSection.getCell(5,0).getChildren().get(0)).getChildren().get(0)).getText();
        String amountHeader = ((Text)((Paragraph)chargeBottomSection.getCell(6,0).getChildren().get(0)).getChildren().get(0)).getText();
        String amountContent = ((Text)((Paragraph)chargeBottomSection.getCell(7,0).getChildren().get(0)).getChildren().get(0)).getText();
        String interestRateHeader = ((Text)((Paragraph)chargeBottomSection.getCell(8,0).getChildren().get(0)).getChildren().get(0)).getText();
        String interestRateContent = ((Text)((Paragraph)chargeBottomSection.getCell(9,0).getChildren().get(0)).getChildren().get(0)).getText();

        String chargeExtentText = ((Text)((Paragraph)chargeExtentTable.getCell(0,0).getChildren().get(1)).getChildren().get(0)).getText();

        assertThat(landRegistryReferenceHeader, is("HM Land Registry reference"));
        assertThat(landRegistryReferenceContent, is(localLandCharge.getDisplayId()));
        assertThat(originatingAuthorityHeader, is("Originating authority"));
        assertThat(originatingAuthorityContent, is(localLandCharge.getItem().getOriginatingAuthority()));
        assertThat(authorityReferenceHeader, is("Authority reference"));
        assertThat(authorityReferenceContent, is(localLandCharge.getItem().getFurtherInformationReference()));
        assertThat(creationDateHeader, is("Creation date"));
        assertThat(creationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(localLandCharge.getItem().getChargeCreationDate())));
        assertThat(registrationDateHeader, is("Registration date"));
        assertThat(registrationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(localLandCharge.getItem().getRegistrationDate())));
        assertThat(categoryHeader, is("Category"));
        assertThat(categoryContent, is(category));
        assertThat(lawHeader, is("Law"));
        assertThat(lawContent, is(localLandCharge.getItem().getStatutoryProvision()));
        assertThat(legalDocumentHeader, is("Legal document"));
        assertThat(legalDocumentContent, is(localLandCharge.getItem().getInstrument()));
        assertThat(locationHeader, is("Location"));
        assertThat(locationContent, is(localLandCharge.getItem().getChargeGeographicDescription()));
        assertThat(descriptionHeader, is("Description"));
        assertThat(descriptionContent, is(localLandCharge.getItem().getSupplementaryInformation()));
        assertThat(amountHeader, is("Amount"));
        assertThat(amountContent, is(Formatters.formatAmount(localLandCharge.getItem().getAmountOriginallySecured())));
        assertThat(interestRateHeader, is("Interest rate"));
        assertThat(interestRateContent, is(Formatters.formatPercentage(localLandCharge.getItem().getRateOfInterest())));

        assertThat(chargeExtentText, is("Charge extent is outlined on the map"));

        verifyAll();
    }
    @Test
    public void testGenerateLandCompensationS2() throws IOException, ImageGenerationException {
        LocalLandCharge localLandCharge = TestUtils.buildLocalLandCharge();
        localLandCharge.getItem().setLandSoldDescription("Some house");
        localLandCharge.getItem().setLandWorksParticulars("Road made");
        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);
        EasyMock.expect(properties.setAlternateDescription("Map showing extent for local land charge abc")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);
        replayAll();

        Table table = LlcSection.generate(localLandCharge, image);
        Table chargeTopSection = (Table)table.getCell(0, 0).getChildren().get(0);
        Table chargeDetailsTable = (Table)chargeTopSection.getCell(0,0).getChildren().get(0);
        Table chargeExtentTable = (Table)chargeTopSection.getCell(0,1).getChildren().get(0);
        Table chargeBottomSection = (Table)table.getCell(1, 0).getChildren().get(0);

        String category = localLandCharge.getItem().getChargeType();
        String subCategory = localLandCharge.getItem().getChargeSubCategory();
        if (subCategory != null) {
        	category = category + " - " + subCategory;
        }

        String landRegistryReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landRegistryReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(1,0).getChildren().get(0)).getChildren().get(0)).getText();
        String originatingAuthorityHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(2,0).getChildren().get(0)).getChildren().get(0)).getText();
        String originatingAuthorityContent = ((Text)((Paragraph)chargeDetailsTable.getCell(3,0).getChildren().get(0)).getChildren().get(0)).getText();
        String authorityReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(4,0).getChildren().get(0)).getChildren().get(0)).getText();
        String authorityReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(5,0).getChildren().get(0)).getChildren().get(0)).getText();
        String creationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(6,0).getChildren().get(0)).getChildren().get(0)).getText();
        String creationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(7,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(8,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(9,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(10,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryContent = ((Text)((Paragraph)chargeDetailsTable.getCell(11,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(12,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawContent = ((Text)((Paragraph)chargeDetailsTable.getCell(13,0).getChildren().get(0)).getChildren().get(0)).getText();

        String legalDocumentHeader = ((Text)((Paragraph)chargeBottomSection.getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String legalDocumentContent = ((Text)((Paragraph)chargeBottomSection.getCell(1,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationHeader = ((Text)((Paragraph)chargeBottomSection.getCell(2,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationContent = ((Text)((Paragraph)chargeBottomSection.getCell(3,0).getChildren().get(0)).getChildren().get(0)).getText();
        String descriptionHeader = ((Text)((Paragraph)chargeBottomSection.getCell(4,0).getChildren().get(0)).getChildren().get(0)).getText();
        String descriptionContent = ((Text)((Paragraph)chargeBottomSection.getCell(5,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landSoldDescriptionHeader = ((Text)((Paragraph)chargeBottomSection.getCell(6,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landSoldDescriptionContent = ((Text)((Paragraph)chargeBottomSection.getCell(7,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landWorksParticularsHeader = ((Text)((Paragraph)chargeBottomSection.getCell(8,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landWorksParticularsContent = ((Text)((Paragraph)chargeBottomSection.getCell(9,0).getChildren().get(0)).getChildren().get(0)).getText();

        String chargeExtentText = ((Text)((Paragraph)chargeExtentTable.getCell(0,0).getChildren().get(1)).getChildren().get(0)).getText();

        assertThat(landRegistryReferenceHeader, is("HM Land Registry reference"));
        assertThat(landRegistryReferenceContent, is(localLandCharge.getDisplayId()));
        assertThat(originatingAuthorityHeader, is("Originating authority"));
        assertThat(originatingAuthorityContent, is(localLandCharge.getItem().getOriginatingAuthority()));
        assertThat(authorityReferenceHeader, is("Authority reference"));
        assertThat(authorityReferenceContent, is(localLandCharge.getItem().getFurtherInformationReference()));
        assertThat(creationDateHeader, is("Creation date"));
        assertThat(creationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(localLandCharge.getItem().getChargeCreationDate())));
        assertThat(registrationDateHeader, is("Registration date"));
        assertThat(registrationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(localLandCharge.getItem().getRegistrationDate())));
        assertThat(categoryHeader, is("Category"));
        assertThat(categoryContent, is(category));
        assertThat(lawHeader, is("Law"));
        assertThat(lawContent, is(localLandCharge.getItem().getStatutoryProvision()));
        assertThat(legalDocumentHeader, is("Legal document"));
        assertThat(legalDocumentContent, is(localLandCharge.getItem().getInstrument()));
        assertThat(locationHeader, is("Location"));
        assertThat(locationContent, is(localLandCharge.getItem().getChargeGeographicDescription()));
        assertThat(descriptionHeader, is("Description"));
        assertThat(descriptionContent, is(localLandCharge.getItem().getSupplementaryInformation()));
        assertThat(landSoldDescriptionHeader, is("Land sold"));
        assertThat(landSoldDescriptionContent, is(localLandCharge.getItem().getLandSoldDescription()));
        assertThat(landWorksParticularsHeader, is("Work done"));
        assertThat(landWorksParticularsContent, is(localLandCharge.getItem().getLandWorksParticulars()));

        assertThat(chargeExtentText, is("Charge extent is outlined on the map"));
        
        verifyAll();
    }
    @Test
    public void testGenerateLandCompensationS52() throws IOException, ImageGenerationException {
        LocalLandCharge localLandCharge = TestUtils.buildLocalLandCharge();
        localLandCharge.getItem().setLandCompensationPaid("123456789");
        localLandCharge.getItem().setAmountOfCompensation("500");
        localLandCharge.getItem().setLandCompensationAmountType("Agreed");
        localLandCharge.getItem().setLandCapacityDescription("Owner");
        localLandCharge.getItem().setStatutoryProvision("Land Compensation Act 1973 section 52(8)");
        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);
        EasyMock.expect(properties.setAlternateDescription("Map showing extent for local land charge abc")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);
        replayAll();

        Table table = LlcSection.generate(localLandCharge, image);
        Table chargeTopSection = (Table)table.getCell(0, 0).getChildren().get(0);
        Table chargeDetailsTable = (Table)chargeTopSection.getCell(0,0).getChildren().get(0);
        Table chargeExtentTable = (Table)chargeTopSection.getCell(0,1).getChildren().get(0);
        Table chargeBottomSection = (Table)table.getCell(1, 0).getChildren().get(0);

        String category = localLandCharge.getItem().getChargeType();
        String subCategory = localLandCharge.getItem().getChargeSubCategory();
        if (subCategory != null) {
        	category = category + " - " + subCategory;
        }

        String landRegistryReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landRegistryReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(1,0).getChildren().get(0)).getChildren().get(0)).getText();
        String originatingAuthorityHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(2,0).getChildren().get(0)).getChildren().get(0)).getText();
        String originatingAuthorityContent = ((Text)((Paragraph)chargeDetailsTable.getCell(3,0).getChildren().get(0)).getChildren().get(0)).getText();
        String authorityReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(4,0).getChildren().get(0)).getChildren().get(0)).getText();
        String authorityReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(5,0).getChildren().get(0)).getChildren().get(0)).getText();
        String creationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(6,0).getChildren().get(0)).getChildren().get(0)).getText();
        String creationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(7,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(8,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(9,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(10,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryContent = ((Text)((Paragraph)chargeDetailsTable.getCell(11,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(12,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawContent = ((Text)((Paragraph)chargeDetailsTable.getCell(13,0).getChildren().get(0)).getChildren().get(0)).getText();

        String legalDocumentHeader = ((Text)((Paragraph)chargeBottomSection.getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String legalDocumentContent = ((Text)((Paragraph)chargeBottomSection.getCell(1,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationHeader = ((Text)((Paragraph)chargeBottomSection.getCell(2,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationContent = ((Text)((Paragraph)chargeBottomSection.getCell(3,0).getChildren().get(0)).getChildren().get(0)).getText();
        String descriptionHeader = ((Text)((Paragraph)chargeBottomSection.getCell(4,0).getChildren().get(0)).getChildren().get(0)).getText();
        String descriptionContent = ((Text)((Paragraph)chargeBottomSection.getCell(5,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCompensationPaidHeader = ((Text)((Paragraph)chargeBottomSection.getCell(6,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCompensationPaidContent = ((Text)((Paragraph)chargeBottomSection.getCell(7,0).getChildren().get(0)).getChildren().get(0)).getText();
        String amountOfCompHeader = ((Text)((Paragraph)chargeBottomSection.getCell(8,0).getChildren().get(0)).getChildren().get(0)).getText();
        String amountOfCompContent = ((Text)((Paragraph)chargeBottomSection.getCell(9,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCompensationAmountTypeHeader = ((Text)((Paragraph)chargeBottomSection.getCell(10,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCompensationAmountTypeContent = ((Text)((Paragraph)chargeBottomSection.getCell(11,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCapacityDescriptionHeader = ((Text)((Paragraph)chargeBottomSection.getCell(12,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCapacityDescriptionContent = ((Text)((Paragraph)chargeBottomSection.getCell(13,0).getChildren().get(0)).getChildren().get(0)).getText();

        String chargeExtentText = ((Text)((Paragraph)chargeExtentTable.getCell(0,0).getChildren().get(1)).getChildren().get(0)).getText();

        assertThat(landRegistryReferenceHeader, is("HM Land Registry reference"));
        assertThat(landRegistryReferenceContent, is(localLandCharge.getDisplayId()));
        assertThat(originatingAuthorityHeader, is("Originating authority"));
        assertThat(originatingAuthorityContent, is(localLandCharge.getItem().getOriginatingAuthority()));
        assertThat(authorityReferenceHeader, is("Authority reference"));
        assertThat(authorityReferenceContent, is(localLandCharge.getItem().getFurtherInformationReference()));
        assertThat(creationDateHeader, is("Creation date"));
        assertThat(creationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(localLandCharge.getItem().getChargeCreationDate())));
        assertThat(registrationDateHeader, is("Registration date"));
        assertThat(registrationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(localLandCharge.getItem().getRegistrationDate())));
        assertThat(categoryHeader, is("Category"));
        assertThat(categoryContent, is(category));
        assertThat(lawHeader, is("Law"));
        assertThat(lawContent, is(localLandCharge.getItem().getStatutoryProvision()));
        assertThat(legalDocumentHeader, is("Legal document"));
        assertThat(legalDocumentContent, is(localLandCharge.getItem().getInstrument()));
        assertThat(locationHeader, is("Location"));
        assertThat(locationContent, is(localLandCharge.getItem().getChargeGeographicDescription()));
        assertThat(descriptionHeader, is("Description"));
        assertThat(descriptionContent, is(localLandCharge.getItem().getSupplementaryInformation()));
        assertThat(landCompensationPaidHeader, is("Advance payment"));
        assertThat(landCompensationPaidContent, is(Formatters.formatAmount(localLandCharge.getItem().getLandCompensationPaid())));
        assertThat(amountOfCompHeader, is("Total compensation"));
        assertThat(amountOfCompContent, is(Formatters.formatAmount(localLandCharge.getItem().getAmountOfCompensation())));
        assertThat(landCompensationAmountTypeHeader, is("Agreed or estimated"));
        assertThat(landCompensationAmountTypeContent, is(localLandCharge.getItem().getLandCompensationAmountType()));
        assertThat(landCapacityDescriptionHeader, is("Interest in land"));
        assertThat(landCapacityDescriptionContent, is(localLandCharge.getItem().getLandCapacityDescription()));

        assertThat(chargeExtentText, is("Charge extent is outlined on the map"));

        verifyAll();
    }

    @Test
    public void testGenerateLandCompensationS52AmountNotProvided() throws IOException, ImageGenerationException {
        LocalLandCharge localLandCharge = TestUtils.buildLocalLandCharge();
        localLandCharge.getItem().setLandCompensationPaid("123456789");
        localLandCharge.getItem().setLandCompensationAmountType("Agreed");
        localLandCharge.getItem().setLandCapacityDescription("Owner");
        localLandCharge.getItem().setStatutoryProvision("Land Compensation Act 1973 section 52(8)");
        Image image = mock(Image.class);
        AccessibilityProperties properties = mock(AccessibilityProperties.class);
        EasyMock.expect(properties.setAlternateDescription("Map showing extent for local land charge abc")).andReturn(properties);
        EasyMock.expect(image.getAccessibilityProperties()).andReturn(properties);
        replayAll();

        Table table = LlcSection.generate(localLandCharge, image);
        Table chargeTopSection = (Table)table.getCell(0, 0).getChildren().get(0);
        Table chargeDetailsTable = (Table)chargeTopSection.getCell(0,0).getChildren().get(0);
        Table chargeExtentTable = (Table)chargeTopSection.getCell(0,1).getChildren().get(0);
        Table chargeBottomSection = (Table)table.getCell(1, 0).getChildren().get(0);

        String category = localLandCharge.getItem().getChargeType();
        String subCategory = localLandCharge.getItem().getChargeSubCategory();
        if (subCategory != null) {
            category = category + " - " + subCategory;
        }

        String landRegistryReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landRegistryReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(1,0).getChildren().get(0)).getChildren().get(0)).getText();
        String originatingAuthorityHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(2,0).getChildren().get(0)).getChildren().get(0)).getText();
        String originatingAuthorityContent = ((Text)((Paragraph)chargeDetailsTable.getCell(3,0).getChildren().get(0)).getChildren().get(0)).getText();
        String authorityReferenceHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(4,0).getChildren().get(0)).getChildren().get(0)).getText();
        String authorityReferenceContent = ((Text)((Paragraph)chargeDetailsTable.getCell(5,0).getChildren().get(0)).getChildren().get(0)).getText();
        String creationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(6,0).getChildren().get(0)).getChildren().get(0)).getText();
        String creationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(7,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(8,0).getChildren().get(0)).getChildren().get(0)).getText();
        String registrationDateContent = ((Text)((Paragraph)chargeDetailsTable.getCell(9,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(10,0).getChildren().get(0)).getChildren().get(0)).getText();
        String categoryContent = ((Text)((Paragraph)chargeDetailsTable.getCell(11,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawHeader = ((Text)((Paragraph)chargeDetailsTable.getCell(12,0).getChildren().get(0)).getChildren().get(0)).getText();
        String lawContent = ((Text)((Paragraph)chargeDetailsTable.getCell(13,0).getChildren().get(0)).getChildren().get(0)).getText();

        String legalDocumentHeader = ((Text)((Paragraph)chargeBottomSection.getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText();
        String legalDocumentContent = ((Text)((Paragraph)chargeBottomSection.getCell(1,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationHeader = ((Text)((Paragraph)chargeBottomSection.getCell(2,0).getChildren().get(0)).getChildren().get(0)).getText();
        String locationContent = ((Text)((Paragraph)chargeBottomSection.getCell(3,0).getChildren().get(0)).getChildren().get(0)).getText();
        String descriptionHeader = ((Text)((Paragraph)chargeBottomSection.getCell(4,0).getChildren().get(0)).getChildren().get(0)).getText();
        String descriptionContent = ((Text)((Paragraph)chargeBottomSection.getCell(5,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCompensationPaidHeader = ((Text)((Paragraph)chargeBottomSection.getCell(6,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCompensationPaidContent = ((Text)((Paragraph)chargeBottomSection.getCell(7,0).getChildren().get(0)).getChildren().get(0)).getText();
        String amountOfCompHeader = ((Text)((Paragraph)chargeBottomSection.getCell(8,0).getChildren().get(0)).getChildren().get(0)).getText();
        String amountOfCompContent = ((Text)((Paragraph)chargeBottomSection.getCell(9,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCompensationAmountTypeHeader = ((Text)((Paragraph)chargeBottomSection.getCell(10,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCompensationAmountTypeContent = ((Text)((Paragraph)chargeBottomSection.getCell(11,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCapacityDescriptionHeader = ((Text)((Paragraph)chargeBottomSection.getCell(12,0).getChildren().get(0)).getChildren().get(0)).getText();
        String landCapacityDescriptionContent = ((Text)((Paragraph)chargeBottomSection.getCell(13,0).getChildren().get(0)).getChildren().get(0)).getText();

        String chargeExtentText = ((Text)((Paragraph)chargeExtentTable.getCell(0,0).getChildren().get(1)).getChildren().get(0)).getText();

        assertThat(landRegistryReferenceHeader, is("HM Land Registry reference"));
        assertThat(landRegistryReferenceContent, is(localLandCharge.getDisplayId()));
        assertThat(originatingAuthorityHeader, is("Originating authority"));
        assertThat(originatingAuthorityContent, is(localLandCharge.getItem().getOriginatingAuthority()));
        assertThat(authorityReferenceHeader, is("Authority reference"));
        assertThat(authorityReferenceContent, is(localLandCharge.getItem().getFurtherInformationReference()));
        assertThat(creationDateHeader, is("Creation date"));
        assertThat(creationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(localLandCharge.getItem().getChargeCreationDate())));
        assertThat(registrationDateHeader, is("Registration date"));
        assertThat(registrationDateContent, is(new SimpleDateFormat("d MMMM yyyy").format(localLandCharge.getItem().getRegistrationDate())));
        assertThat(categoryHeader, is("Category"));
        assertThat(categoryContent, is(category));
        assertThat(lawHeader, is("Law"));
        assertThat(lawContent, is(localLandCharge.getItem().getStatutoryProvision()));
        assertThat(legalDocumentHeader, is("Legal document"));
        assertThat(legalDocumentContent, is(localLandCharge.getItem().getInstrument()));
        assertThat(locationHeader, is("Location"));
        assertThat(locationContent, is(localLandCharge.getItem().getChargeGeographicDescription()));
        assertThat(descriptionHeader, is("Description"));
        assertThat(descriptionContent, is(localLandCharge.getItem().getSupplementaryInformation()));
        assertThat(landCompensationPaidHeader, is("Advance payment"));
        assertThat(landCompensationPaidContent, is(Formatters.formatAmount(localLandCharge.getItem().getLandCompensationPaid())));
        assertThat(amountOfCompHeader, is("Total compensation"));
        assertThat(amountOfCompContent, is("Not provided"));
        assertThat(landCompensationAmountTypeHeader, is("Agreed or estimated"));
        assertThat(landCompensationAmountTypeContent, is(localLandCharge.getItem().getLandCompensationAmountType()));
        assertThat(landCapacityDescriptionHeader, is("Interest in land"));
        assertThat(landCapacityDescriptionContent, is(localLandCharge.getItem().getLandCapacityDescription()));

        assertThat(chargeExtentText, is("Charge extent is outlined on the map"));

        verifyAll();
    }
}
