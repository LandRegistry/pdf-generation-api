package pdfgenerator.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "line-1",
        "line-2",
        "line-3",
        "line-4",
        "line-5",
        "line-6",
        "postcode",
        "country"
})
public class Address {
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

    @JsonProperty("country")
    private String country;

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

    @JsonSetter("country")
    public void setCountry(String country) { this.country = country; }

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
    public String getPostcode() { return postcode; }

    @JsonGetter("country")
    public String getCountry() { return country; }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Builds and returns the Address string to be displayed by LON Sections.
     * @return The formatted address
     */
    public String toAddressString() {
        String result = this.line1;

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

        if(this.postcode != null && !this.postcode.isEmpty()) {
            result += System.lineSeparator() + this.postcode;
        }

        result += System.lineSeparator() + this.country;
        return result;
    }

    /**
     * Builds and returns the Address object from a provided json string. This is required for
     * scenario where address object is provided inside of a string field
     * e.g. for LON's, charge-geographic-description will contain a json string of this object.
     * TODO: This function should be removed when a new field is added for LON dominant building address
     * @param json The json to build the Address from.
     * @return The Address.
     * @throws IOException If json cannot be parsed.
     */
    public static Address buildAddressFrom(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Address.class);
    }
}
