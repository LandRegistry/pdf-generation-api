package pdfgenerator.models;

import com.fasterxml.jackson.annotation.*;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "item",
    "display_id"
})
public class LocalLandCharge implements Comparable<LocalLandCharge> {
    @JsonProperty("item")
    private Item item;

    @JsonProperty("display_id")
    private String displayId;

    @JsonProperty("cancelled")
    private Boolean cancelled;

    @JsonProperty("geometry")
    private Geometry geometry;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("type")
    private String type;

    @JsonGetter("item")
    public Item getItem() {
        return item;
    }

    @JsonSetter("item")
    public void setItem(Item item) {
        this.item = item;
    }

    @JsonGetter("display_id")
    public String getDisplayId() {
        return displayId;
    }

    @JsonSetter("display_id")
    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    @JsonGetter("cancelled")
    public Boolean getCancelled() {
        return cancelled;
    }

    @JsonSetter("cancelled")
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    @JsonGetter("geometry")
    public Geometry getGeometry() {
        return geometry;
    }

    @JsonSetter("geometry")
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @JsonGetter("id")
    public Integer getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonGetter("type")
    public String getType() {
        return type;
    }

    @JsonSetter("type")
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(LocalLandCharge o) {
        Date dateOne = getItem().getChargeCreationDate();
        Date dateTwo = o.getItem().getChargeCreationDate();

        if (dateOne == null && dateTwo == null) {
        	return 0;
        }
        if (dateOne == null) {
            return 1;
        }
        if (dateTwo == null) {
            return -1;
        }

        Long dateTimeOne = dateOne.getTime();
        Long dateTimeTwo = dateTwo.getTime();

        return dateTimeTwo.compareTo(dateTimeOne);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
