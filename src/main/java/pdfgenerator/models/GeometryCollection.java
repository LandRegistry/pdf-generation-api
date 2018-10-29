package pdfgenerator.models;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pdfgenerationapi.models.Geometry;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "type",
    "geometries"
})
public class GeometryCollection {
    @JsonProperty("type")
    private String type = "GeometryCollection";

    @JsonProperty("geometries")
    private List<Geometry> geometries = new ArrayList<>(1);

    @JsonGetter("geometries")
    public List<Geometry> getGeometries() {
        return geometries;
    }

    @JsonSetter("geometries")
    public void setFeatures(List<Geometry> geometries) {
        this.geometries = geometries;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
