package pdfgenerator.models;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "bucket",
    "file_id",
    "reference",
    "subdirectory"
})
public class DocumentItem {

    @JsonProperty("bucket")
    private String bucket;

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("reference")
    private String reference;

    @JsonProperty("subdirectory")
    private String subdirectory;

    @JsonGetter("bucket")
    public String getBucket() { return bucket; }

    @JsonSetter("bucket")
    public void setBucket(String bucket) { this.bucket = bucket; }

    @JsonGetter("file_id")
    public String getFileId() { return fileId; }

    @JsonSetter("file_id")
    public void setFileId(String fileId) { this.fileId = fileId; }

    @JsonGetter("reference")
    public String getReference() { return reference; }

    @JsonSetter("reference")
    public void setReference(String reference) { this.reference = reference; }

    @JsonGetter("subdirectory")
    public String getSubdirectory() { return subdirectory; }

    @JsonSetter("subdirectory")
    public void setSubdirectory(String subdirectory) { this.subdirectory = subdirectory; }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
