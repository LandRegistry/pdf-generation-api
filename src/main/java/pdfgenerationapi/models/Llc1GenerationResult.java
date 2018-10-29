package pdfgenerationapi.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import pdfgenerator.models.LocalLandCharge;

@JsonPropertyOrder ({
    "included_charges",
    "document_url",
    "external_url"
})
public class Llc1GenerationResult {

    @JsonProperty("document_url")
    private String documentUrl;

    @JsonProperty("included_charges")
    private List<LocalLandCharge> includedCharges;

    @JsonProperty("external_url")
    private String externalUrl;

    public Llc1GenerationResult(String documentUrl, String externalUrl, List<LocalLandCharge> includedCharges) {
        this.documentUrl = documentUrl;
        this.includedCharges = includedCharges;
        this.externalUrl = externalUrl;
    }

    @JsonGetter("document_url")
    public String getDocumentUrl() {
        return documentUrl;
    }

    @JsonSetter("document_url")
    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    @JsonGetter("external_url")
    public String getExternalUrl() {
        return externalUrl;
    }

    @JsonSetter("external_url")
    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    @JsonGetter("included_charges")
    public List<LocalLandCharge> getIncludedCharges() { return includedCharges; }

    @JsonSetter("included_charges")
    public void setIncludedCharges(List<LocalLandCharge> includedCharges) { this.includedCharges = includedCharges; }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
