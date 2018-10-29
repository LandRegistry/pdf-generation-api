package pdfgenerator.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

public class TestItem {

    @Test
    public void testInitialize() {
        Item item = new Item();
        Author author = new Author();
        Date date = new Date();
        Geometry geometry = new Geometry();
        Address address = new Address();

        DocumentItem[] documentItems =  new DocumentItem[] { new DocumentItem() };

        HashMap<String, DocumentItem[]> documentsFiled = new HashMap<>();
        documentsFiled.put("LLC-0", documentItems);

        item.setAuthor(author);
        item.setChargeCreationDate(date);
        item.setChargeGeographicDescription("charge Geo Description");
        item.setChargeType("charge type");
        item.setSupplementaryInformation("supplementary information");
        item.setFurtherInformationLocation("further information location");
        item.setFurtherInformationReference("further information ref");
        item.setGeometry(geometry);
        item.setInstrument("instrument");
        item.setLocalLandCharge("local land charge");
        item.setOriginatingAuthority("originating authority");
        item.setRegistrationDate(date);
        item.setStartDate(date);
        item.setStatutoryProvision("Statutory provision");
        item.setExpiryDate(date);
        item.setApplicantName("applicant name");
        item.setApplicantAddress(address);
        item.setServientLandInterestDescription("servient land");
        item.setTribunalDefinitiveCertificateDate(date);
        item.setTribunalTemporaryCertificateDate(date);
        item.setTribunalTemporaryCertificateExpiryDate(date);
        item.setDocumentsFiled(documentsFiled);

        assertThat(item.getAuthor(), is(author));
        assertThat(item.getChargeCreationDate(), is(date));
        assertThat(item.getChargeGeographicDescription(), is("charge Geo Description"));
        assertThat(item.getChargeType(), is("charge type"));
        assertThat(item.getSupplementaryInformation(), is("supplementary information"));
        assertThat(item.getFurtherInformationLocation(), is("further information location"));
        assertThat(item.getFurtherInformationReference(), is("further information ref"));
        assertThat(item.getGeometry(), is(geometry));
        assertThat(item.getInstrument(), is("instrument"));
        assertThat(item.getLocalLandCharge(), is("local land charge"));
        assertThat(item.getOriginatingAuthority(), is("originating authority"));
        assertThat(item.getRegistrationDate(), is(date));
        assertThat(item.getStartDate(), is(date));
        assertThat(item.getStatutoryProvision(), is("Statutory provision"));
        assertThat(item.getExpiryDate(), is(date));
        assertThat(item.getApplicantName(), is("applicant name"));
        assertThat(item.getApplicantAddress(), is(address));
        assertThat(item.getServientLandInterestDescription(), is("servient land"));
        assertThat(item.getTribunalDefinitiveCertificateDate(), is(date));
        assertThat(item.getTribunalTemporaryCertificateDate(), is(date));
        assertThat(item.getTribunalTemporaryCertificateExpiryDate(), is(date));
        assertThat(item.getDocumentsFiled(), is(documentsFiled));
    }

    @Test
    public void testInitializeFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"further-information-reference\":\"further information ref\"," +
                    "\"registration-date\":1497613093048," +
                    "\"author\":{\"organisation\":null,\"full-name\":null,\"email\":null}," +
                    "\"further-information-location\":\"further information location\"," +
                    "\"start-date\":1497613093048," +
                    "\"instrument\":\"instrument\"," +
                    "\"charge-geographic-description\":\"charge Geo Description\"," +
                    "\"charge-type\":\"charge type\"," +
                    "\"charge-creation-date\":1497613093048," +
                    "\"originating-authority\":\"originating authority\"," +
                    "\"geometry\":{\"type\":null,\"features\":[]}," +
                    "\"statutory-provision\":\"Statutory provision\"," +
                    "\"servient-land-interest-description\": \"servient land\"," +
                    "\"structure-position-and-dimension\": {\"height\": \"Unlimited height\", \"extent-covered\": \"All of the extent\"}," +
                    "\"applicant-address\": {\"line-1\": \"street\", \"line-2\": \"town\", \"country\": \"country\", \"postcode\": \"postcode\"}," +
                    "\"applicant-name\": \"James\"," +
                    "\"expiry-date\": \"2016-01-01\"," +
                    "\"tribunal-definitive-certificate-date\": \"2016-01-01\"," +
                    "\"tribunal-temporary-certificate-date\": \"2016-01-01\"," +
                    "\"tribunal-temporary-certificate-expiry-date\": \"2016-01-01\"," +
                    "\"local-land-charge\":\"local land charge\"," +
                    "\"documents-filed\": {\"form-a\": [ { \"bucket\": \"bucket\", \"reference\": \"reference\", \"subdirectory\": \"subdirectory\", \"file-id\": \"id\" } ] } }";
        Item item = mapper.readValue(json,Item.class);

        assertThat(item.getAuthor(), is(not(nullValue())));
        assertThat(item.getChargeCreationDate(), is(not(nullValue())));
        assertThat(item.getChargeGeographicDescription(), is("charge Geo Description"));
        assertThat(item.getChargeType(), is("charge type"));
        assertThat(item.getFurtherInformationLocation(), is("further information location"));
        assertThat(item.getFurtherInformationReference(), is("further information ref"));
        assertThat(item.getGeometry(), is(not(nullValue())));
        assertThat(item.getInstrument(), is("instrument"));
        assertThat(item.getLocalLandCharge(), is("local land charge"));
        assertThat(item.getOriginatingAuthority(), is("originating authority"));
        assertThat(item.getRegistrationDate(), is(not(nullValue())));
        assertThat(item.getStartDate(), is(not(nullValue())));
        assertThat(item.getStatutoryProvision(), is("Statutory provision"));
        assertThat(item.getExpiryDate(), is(not(nullValue())));
        assertThat(item.getApplicantName(), is("James"));
        assertThat(item.getApplicantAddress(), is(not(nullValue())));
        assertThat(item.getServientLandInterestDescription(), is("servient land"));
        assertThat(item.getTribunalDefinitiveCertificateDate(), is(not(nullValue())));
        assertThat(item.getTribunalTemporaryCertificateDate(), is(not(nullValue())));
        assertThat(item.getTribunalTemporaryCertificateExpiryDate(), is(not(nullValue())));
        assertThat(item.getDocumentsFiled(), is(not(nullValue())));
    }

    @Test
    public void testToString() {
        Item item = new Item();
        Author author = new Author();
        Date date = new Date();
        Geometry geometry = new Geometry();

        DocumentItem[] documentItems =  new DocumentItem[] { new DocumentItem() };

        HashMap<String, DocumentItem[]> documentsFiled = new HashMap<>();
        documentsFiled.put("LLC-0", documentItems);

        item.setAuthor(author);
        item.setChargeCreationDate(date);
        item.setChargeGeographicDescription("charge Geo Description");
        item.setChargeType("charge type");
        item.setFurtherInformationLocation("further information location");
        item.setFurtherInformationReference("further information ref");
        item.setGeometry(geometry);
        item.setInstrument("instrument");
        item.setLocalLandCharge("local land charge");
        item.setOriginatingAuthority("originating authority");
        item.setRegistrationDate(date);
        item.setStartDate(date);
        item.setStatutoryProvision("Statutory provision");
        item.setDocumentsFiled(documentsFiled);

        String result = item.toString();

        assertThat(result, containsStringIgnoringCase("furtherInformationReference=further information ref"));
        assertThat(result, containsStringIgnoringCase("registrationDate=" + date.toString()));
        assertThat(result, containsStringIgnoringCase("author=pdfgenerator.models.Author"));
        assertThat(result, containsStringIgnoringCase("furtherInformationLocation=further information location"));
        assertThat(result, containsStringIgnoringCase("startDate=" + date.toString()));
        assertThat(result, containsStringIgnoringCase("instrument=instrument"));
        assertThat(result, containsStringIgnoringCase("chargeGeographicDescription=charge Geo Description"));
        assertThat(result, containsStringIgnoringCase("chargeType=charge type"));
        assertThat(result, containsStringIgnoringCase("chargeCreationDate=" + date.toString()));
        assertThat(result, containsStringIgnoringCase("originatingAuthority=originating authority"));
        assertThat(result, containsStringIgnoringCase("geometry=pdfgenerator.models.Geometry"));
        assertThat(result, containsStringIgnoringCase("statutoryProvision=Statutory provision"));
        assertThat(result, containsStringIgnoringCase("localLandCharge=local land charge"));
        assertThat(result, containsStringIgnoringCase("documentsFiled={LLC-0=[Lpdfgenerator.models.DocumentItem;"));
    }
}
