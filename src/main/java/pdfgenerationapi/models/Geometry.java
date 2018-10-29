package pdfgenerationapi.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {

    @JsonProperty("type")
    private String type;

    @JsonProperty("coordinates")
    private List<Object> coordinates;

    @JsonGetter("type")
    public String getType() {return type;}

    @JsonSetter("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonGetter("coordinates")
    public List<Object> getCoordinates() {
        return coordinates;
    }

    @JsonSetter("coordinates")
    public void setCoordinates(List<Object> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
