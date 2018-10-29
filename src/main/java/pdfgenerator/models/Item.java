package pdfgenerator.models;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "further-information-reference",
    "registration-date",
    "author",
    "further-information-location",
    "start-date",
    "instrument",
    "charge-address",
    "charge-geographic-description",
    "charge-type",
    "charge-creation-date",
    "originating-authority",
    "geometry",
    "statutory-provision",
    "expiry-date",
    "applicant-name",
    "applicant-address",
    "servient-land-interest-description",
    "structure-position-and-dimension",
    "tribunal-definitive-certificate-date",
    "tribunal-temporary-certificate-date",
    "tribunal-temporary-certificate-expiry-date",
    "local-land-charge",
    "amount-originally-secured",
    "rate-of-interest",
    "land-sold-description",
    "land-works-particulars",
    "land-compensation-paid",
    "amount-of-compensation",
    "land-compensation-amount-type",
    "land-capacity-description",
    "charge-sub-category"
})
public class Item {
    @JsonProperty("further-information-reference")
    private String furtherInformationReference;

    @JsonProperty("registration-date")
    private Date registrationDate;

    @JsonProperty("author")
    private Author author;

    @JsonProperty("further-information-location")
    private String furtherInformationLocation;

    @JsonProperty("start-date")
    private Date startDate;

    @JsonProperty("instrument")
    private String instrument;

    @JsonProperty("charge-address")
    private ChargeAddress chargeAddress;

    @JsonProperty("charge-geographic-description")
    private String chargeGeographicDescription;

    @JsonProperty("charge-type")
    private String chargeType;

    @JsonProperty("supplementary-information")
    private String supplementaryInformation;

    @JsonProperty("charge-creation-date")
    private Date chargeCreationDate;

    @JsonProperty("originating-authority")
    private String originatingAuthority;

    @JsonProperty("geometry")
    private Geometry geometry;

    @JsonProperty("statutory-provision")
    private String statutoryProvision;

    @JsonProperty("expiry-date")
    private Date expiryDate;

    @JsonProperty("applicant-name")
    private String applicantName;

    @JsonProperty("applicant-address")
    private Address applicantAddress;

    @JsonProperty("servient-land-interest-description")
    private String servientLandInterestDescription;

    @JsonProperty("structure-position-and-dimension")
    private StructurePositionDimension structurePositionAndDimension;

    @JsonProperty("tribunal-definitive-certificate-date")
    private Date tribunalDefinitiveCertificateDate;

    @JsonProperty("tribunal-temporary-certificate-date")
    private Date tribunalTemporaryCertificateDate;

    @JsonProperty("tribunal-temporary-certificate-expiry-date")
    private Date tribunalTemporaryCertificateExpiryDate;

    @JsonProperty("local-land-charge")
    private String localLandCharge;

    @JsonProperty("documents-filed")
    private Map<String, DocumentItem[]> documentsFiled;

    @JsonProperty("amount-originally-secured")
    private String amountOriginallySecured;

    @JsonProperty("rate-of-interest")
    private String rateOfInterest;

    @JsonProperty("land-sold-description")
    private String landSoldDescription;

    @JsonProperty("land-works-particulars")
    private String landWorksParticulars;

    @JsonProperty("land-compensation-paid")
    private String landCompensationPaid;

    @JsonProperty("amount-of-compensation")
    private String amountOfCompensation;

    @JsonProperty("land-compensation-amount-type")
    private String landCompensationAmountType;

    @JsonProperty("land-capacity-description")
    private String landCapacityDescription;
    
    @JsonProperty("charge-sub-category")
    private String chargeSubCategory;

    @JsonGetter("further-information-reference")
    public String getFurtherInformationReference() {
        return furtherInformationReference;
    }

    @JsonSetter("further-information-reference")
    public void setFurtherInformationReference(String furtherInformationReference) {
        this.furtherInformationReference = furtherInformationReference;
    }

    @JsonGetter("registration-date")
    public Date getRegistrationDate() {
        return registrationDate;
    }

    @JsonSetter("registration-date")
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @JsonGetter("author")
    public Author getAuthor() {
        return author;
    }

    @JsonSetter("author")
    public void setAuthor(Author author) {
        this.author = author;
    }

    @JsonGetter("further-information-location")
    public String getFurtherInformationLocation() {
        return furtherInformationLocation;
    }

    @JsonSetter("further-information-location")
    public void setFurtherInformationLocation(String furtherInformationLocation) {
        // replace \r\n for line feed as this will cause problems in rendering pdf
        this.furtherInformationLocation = furtherInformationLocation.replace("\r\n", "\n");
    }

    @JsonGetter("start-date")
    public Date getStartDate() {
        return startDate;
    }

    @JsonSetter("start-date")
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonGetter("instrument")
    public String getInstrument() {
        return instrument;
    }

    @JsonSetter("instrument")
    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    @JsonGetter("charge-address")
    public ChargeAddress getChargeAddress() {
        return chargeAddress;
    }

    @JsonSetter("charge-address")
    public void setChargeAddress(ChargeAddress chargeAddress) {
        this.chargeAddress = chargeAddress;
    }

    @JsonGetter("charge-geographic-description")
    public String getChargeGeographicDescription() {
        return chargeGeographicDescription;
    }

    @JsonSetter("charge-geographic-description")
    public void setChargeGeographicDescription(String chargeGeographicDescription) {
        // replace \r\n for line feed as this will cause problems in rendering pdf
        this.chargeGeographicDescription = chargeGeographicDescription.replace("\r\n", "\n");
    }

    @JsonGetter("supplementary-information")
    public String getSupplementaryInformation() {
        return supplementaryInformation;
    }

    @JsonSetter("supplementary-information")
    public void setSupplementaryInformation(String supplementaryInformation) {
        // replace \r\n for line feed as this will cause problems in rendering pdf
        this.supplementaryInformation = supplementaryInformation.replace("\r\n", "\n");
    }

    @JsonGetter("charge-type")
    public String getChargeType() {
        return chargeType;
    }

    @JsonSetter("charge-type")
    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    @JsonGetter("charge-creation-date")
    public Date getChargeCreationDate() {
        return chargeCreationDate;
    }

    @JsonSetter("charge-creation-date")
    public void setChargeCreationDate(Date chargeCreationDate) {
        this.chargeCreationDate = chargeCreationDate;
    }

    @JsonGetter("originating-authority")
    public String getOriginatingAuthority() {
        return originatingAuthority;
    }

    @JsonSetter("originating-authority")
    public void setOriginatingAuthority(String originatingAuthority) {
        this.originatingAuthority = originatingAuthority;
    }

    @JsonGetter("geometry")
    public Geometry getGeometry() {
        return geometry;
    }

    @JsonSetter("geometry")
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @JsonGetter("statutory-provision")
    public String getStatutoryProvision() {
        return statutoryProvision;
    }

    @JsonSetter("statutory-provision")
    public void setStatutoryProvision(String statutoryProvision) {
        this.statutoryProvision = statutoryProvision;
    }

    @JsonGetter("expiry-date")
    public Date getExpiryDate() {
        return expiryDate;
    }

    @JsonSetter("expiry-date")
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    @JsonGetter("applicant-name")
    public String getApplicantName() { return applicantName; }

    @JsonSetter("applicant-name")
    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    @JsonGetter("applicant-address")
    public Address getApplicantAddress() { return applicantAddress; }

    @JsonSetter("applicant-address")
    public void setApplicantAddress(Address applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    @JsonGetter("servient-land-interest-description")
    public String getServientLandInterestDescription() { return servientLandInterestDescription; }

    @JsonSetter("servient-land-interest-description")
    public void setServientLandInterestDescription(String servientLandInterestDescription) {
        this.servientLandInterestDescription = servientLandInterestDescription;
    }

    @JsonGetter("structure-position-and-dimension")
    public StructurePositionDimension getStructurePositionAndDimension() {
        return structurePositionAndDimension;
    }

    @JsonSetter("structure-position-and-dimension")
    public void setStructurePositionAndDimension(StructurePositionDimension structurePositionAndDimension) {
        this.structurePositionAndDimension = structurePositionAndDimension;
    }

    @JsonGetter("tribunal-definitive-certificate-date")
    public Date getTribunalDefinitiveCertificateDate() {
        return tribunalDefinitiveCertificateDate;
    }

    @JsonSetter("tribunal-definitive-certificate-date")
    public void setTribunalDefinitiveCertificateDate(Date tribunalDefinitiveCertificateDate) {
        this.tribunalDefinitiveCertificateDate = tribunalDefinitiveCertificateDate;
    }

    @JsonGetter("tribunal-temporary-certificate-date")
    public Date getTribunalTemporaryCertificateDate() {
        return tribunalTemporaryCertificateDate;
    }

    @JsonSetter("tribunal-temporary-certificate-date")
    public void setTribunalTemporaryCertificateDate(Date tribunalTemporaryCertificateDate) {
        this.tribunalTemporaryCertificateDate = tribunalTemporaryCertificateDate;
    }

    @JsonGetter("tribunal-temporary-certificate-expiry-date")
    public Date getTribunalTemporaryCertificateExpiryDate() {
        return tribunalTemporaryCertificateExpiryDate;
    }

    @JsonSetter("tribunal-temporary-certificate-expiry-date")
    public void setTribunalTemporaryCertificateExpiryDate(Date tribunalTemporaryCertificateExpiryDate) {
        this.tribunalTemporaryCertificateExpiryDate = tribunalTemporaryCertificateExpiryDate;
    }

    @JsonGetter("local-land-charge")
    public String getLocalLandCharge() {
        return localLandCharge;
    }

    @JsonSetter("local-land-charge")
    public void setLocalLandCharge(String localLandCharge) {
        this.localLandCharge = localLandCharge;
    }

    @JsonGetter("documents-filed")
    public Map<String, DocumentItem[]> getDocumentsFiled() { return documentsFiled; }

    @JsonSetter("documents-filed")
    public void setDocumentsFiled(Map<String, DocumentItem[]> documentsFiled) { this.documentsFiled = documentsFiled; }
    
    @JsonGetter("amount-originally-secured")
    public String getAmountOriginallySecured() {
    	return amountOriginallySecured;
    }

    @JsonSetter("amount-originally-secured")
    public void setAmountOriginallySecured(String amountOriginallySecured) {
        this.amountOriginallySecured = amountOriginallySecured;
    }

    @JsonGetter("rate-of-interest")
    public String getRateOfInterest() {
    	return rateOfInterest;
    }

    @JsonSetter("rate-of-interest")
    public void setRateOfInterest(String rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    @JsonGetter("land-sold-description")
    public String getLandSoldDescription(){
        return landSoldDescription;
    }

    @JsonSetter("land-sold-description")
    public void setLandSoldDescription(String landSoldDescription){
        // replace \r\n for line feed as this will cause problems in rendering pdf
        this.landSoldDescription = landSoldDescription.replace("\r\n", "\n");
    }

    @JsonGetter("land-works-particulars")
    public String getLandWorksParticulars(){
        return landWorksParticulars;
    }

    @JsonSetter("land-works-particulars")
    public void setLandWorksParticulars(String landWorksParticulars){
        // replace \r\n for line feed as this will cause problems in rendering pdf
        this.landWorksParticulars = landWorksParticulars.replace("\r\n", "\n");
    }

    @JsonGetter("land-compensation-paid")
    public String getLandCompensationPaid(){
        return landCompensationPaid;
    }

    @JsonSetter("land-compensation-paid")
    public void setLandCompensationPaid(String landCompensationPaid){
        this.landCompensationPaid = landCompensationPaid;
    }

    @JsonGetter("amount-of-compensation")
    public String getAmountOfCompensation(){
        return amountOfCompensation;
    }

    @JsonSetter("amount-of-compensation")
    public void setAmountOfCompensation(String amountOfCompensation){
        this.amountOfCompensation = amountOfCompensation;
    }

    @JsonGetter("land-compensation-amount-type")
    public String getLandCompensationAmountType(){
        return landCompensationAmountType;
    }

    @JsonSetter("land-compensation-amount-type")
    public void setLandCompensationAmountType(String landCompensationAmountType){
        this.landCompensationAmountType = landCompensationAmountType;
    }

    @JsonGetter("land-capacity-description")
    public String getLandCapacityDescription(){
        return landCapacityDescription;
    }

    @JsonSetter("land-capacity-description")
    public void setLandCapacityDescription(String landCapacityDescription){
        // replace \r\n for line feed as this will cause problems in rendering pdf
        this.landCapacityDescription = landCapacityDescription.replace("\r\n", "\n");
    }

    @JsonGetter("charge-sub-category")
    public String getChargeSubCategory(){
        return chargeSubCategory;
    }

    @JsonSetter("charge-sub-category")
    public void setChargeSubCategory(String chargeSubCategory){
        this.chargeSubCategory = chargeSubCategory;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
