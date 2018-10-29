package pdfgenerator.models;

import com.fasterxml.jackson.annotation.*;


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({
        "height",
        "units",
        "extent-covered",
        "part-explanatory-text"
})
public class StructurePositionDimension {
    @JsonProperty("height")
    private String height;

    @JsonSetter("height")
    public void setHeight(String height) { this.height = height; }

    @JsonGetter("height")
    public String getHeight() { return this.height; }

    @JsonProperty("units")
    private String units;

    @JsonSetter("units")
    public void setUnits(String units) { this.units = units; }

    @JsonGetter("units")
    public String getUnits() { return this.units; }

    @JsonProperty("extent-covered")
    private String extentCovered;

    @JsonSetter("extent-covered")
    public void setExtentCovered(String extentCovered) { this.extentCovered = extentCovered; }

    @JsonGetter("extent-covered")
    public String getExtentCovered() { return this.extentCovered; }

    @JsonProperty("part-explanatory-text")
    private String partExplanatoryText;

    @JsonSetter("part-explanatory-text")
    public void setPartExplanatoryText(String partExplanatoryText) {
        // replace \r\n for line feed as this will cause problems in rendering pdf
        this.partExplanatoryText = partExplanatoryText.replace("\r\n", "\n");
    }

    @JsonGetter("part-explanatory-text")
    public String getPartExplanatoryText() { return this.partExplanatoryText; }

    public String toPositionString() {
        if(this.extentCovered.equalsIgnoreCase("All of the extent")) {
            return this.extentCovered;
        } else {
            return this.partExplanatoryText;
        }
    }

    public String toHeightString() {
        if(!this.height.equalsIgnoreCase("Unlimited height")) {
            return this.height + " " + this.units;
        } else {
            return this.height;
        }
    }
}