package pdfgenerationapi.models;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

@JsonPropertyOrder({
        "type",
        "features"
})
public class Extents {

    @JsonProperty("type")
    private String type;

    @JsonProperty("features")
    private List<Feature> features = null;

    @JsonGetter("type")
    public String getType() {
        return type;
    }

    @JsonSetter("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonGetter("features")
    public List<Feature> getFeatures() {
        return features;
    }

    @JsonSetter("features")
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
