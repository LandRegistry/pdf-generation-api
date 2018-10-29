package pdfgenerationapi.models;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonPropertyOrder({
        "type",
        "geometry",
        "properties"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Feature {

    @JsonProperty("type")
    private String type;

    @JsonProperty("geometry")
    private Geometry geometry;

    @JsonProperty("properties")
    private Object properties;

    @JsonGetter("type")
    public String getType() {
        return type;
    }

    @JsonSetter("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonGetter("geometry")
    public Geometry getGeometry() {
        return geometry;
    }

    @JsonSetter("geometry")
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @JsonGetter("properties")
    public Object getProperties() {
        return properties;
    }

    @JsonSetter("properties")
    public void setProperties(Object properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
