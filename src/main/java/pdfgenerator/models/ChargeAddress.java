package pdfgenerator.models;

import com.fasterxml.jackson.annotation.*;


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({
        "line-1", 
        "line-2",
        "line-3",
        "line-4",
        "line-5",
        "line-6",
        "postcode",
        "unique-property-reference-number"
})
public class ChargeAddress {
    @JsonProperty("line-1")
    private String line1;

    @JsonProperty("line-2")
    private String line2;

    @JsonProperty("line-3")
    private String line3;

    @JsonProperty("line-4")
    private String line4;

    @JsonProperty("line-5")
    private String line5;

    @JsonProperty("line-6")
    private String line6;

    @JsonProperty("postcode")
    private String postcode;

    @JsonProperty("unique-property-reference-number")
    private String uprn;

    @JsonSetter("line-1")
    public void setLine1(String line1) { this.line1 = line1; }

    @JsonSetter("line-2")
    public void setLine2(String line2) { this.line2 = line2; }

    @JsonSetter("line-3")
    public void setLine3(String line3) { this.line3 = line3; }

    @JsonSetter("line-4")
    public void setLine4(String line4) { this.line4 = line4; }

    @JsonSetter("line-5")
    public void setLine5(String line5) { this.line5 = line5; }

    @JsonSetter("line-6")
    public void setLine6(String line6) { this.line6 = line6; }

    @JsonSetter("postcode")
    public void setPostcode(String postcode) { this.postcode = postcode; }

    @JsonSetter("unique-property-reference-number")
    public void setUprn(String uprn) { this.uprn = uprn; }

    @JsonGetter("line-1")
    public String getLine1() { return this.line1; }

    @JsonGetter("line-2")
    public String getLine2() { return this.line2; }

    @JsonGetter("line-3")
    public String getLine3() { return this.line3; }

    @JsonGetter("line-4")
    public String getLine4() { return this.line4; }

    @JsonGetter("line-5")
    public String getLine5() { return this.line5; }

    @JsonGetter("line-6")
    public String getLine6() { return this.line6; }

    @JsonGetter("postcode")
    public String getPostcode() { return this.postcode; }

    @JsonGetter("unique-property-reference-number")
    public String getUprn() { return this.uprn; }

    public String toAddressString() {
        String result = this.getLine1();

        if(this.line2 != null && !this.line2.isEmpty()) {
            result += System.lineSeparator() + this.line2;
        }

        if(this.line3 != null && !this.line3.isEmpty()) {
            result += System.lineSeparator() + this.line3;
        }

        if(this.line4 != null && !this.line4.isEmpty()) {
            result += System.lineSeparator() + this.line4;
        }

        if(this.line5 != null && !this.line5.isEmpty()) {
            result += System.lineSeparator() + this.line5;
        }

        if(this.line6 != null && !this.line6.isEmpty()) {
            result += System.lineSeparator() + this.line6;
        }

        result += System.lineSeparator() + this.postcode;
        return result;
    }
}