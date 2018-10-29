package pdfgenerationapi.models;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonPropertyOrder({
        "description",
        "extents",
        "referenceNumber"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Llc1PdfRequest {
    @JsonProperty("description")
    private String description;

    @JsonProperty("extents")
    private Extents extents;

    @JsonProperty("referenceNumber")
    private long referenceNumber;

    @JsonGetter("description")
    public String getDescription() { return description; }

    @JsonSetter("description")
    public void setDescription(String description) {
        // replace \r\n for line feed as this will cause problems in rendering pdf
        this.description = description.replace("\r\n", "\n");
    }

    @JsonGetter("extents")
    public Extents getExtents() { return extents; }

    @JsonSetter("extents")
    public void setExtents(Extents extents) { this.extents = extents; }

    @JsonGetter("referenceNumber")
    public long getReferenceNumber() { return referenceNumber; }

    @JsonSetter("referenceNumber")
    public void setReferenceNumber(long referenceNumber) { this.referenceNumber = referenceNumber; }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
