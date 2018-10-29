package pdfgenerator.models;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "organisation",
    "full-name",
    "email"
})
public class Author {
    @JsonProperty("organisation")
    private String organisation;

    @JsonProperty("full-name")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonGetter("organisation")
    public String getOrganisation() {
        return organisation;
    }

    @JsonSetter("organisation")
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    @JsonGetter("full-name")
    public String getFullName() {
        return fullName;
    }

    @JsonSetter("full-name")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonGetter("email")
    public String getEmail() {
        return email;
    }

    @JsonSetter("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "email", "fullName");
    }
}
