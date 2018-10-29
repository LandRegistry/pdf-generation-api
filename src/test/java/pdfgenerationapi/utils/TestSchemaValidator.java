package pdfgenerationapi.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;


public class TestSchemaValidator {

    @Test
    public void TestInstanceValid() throws IOException, ProcessingException {
        SchemaValidator validator = SchemaValidator.getInstance();
        assertThat(validator, is(not(nullValue())));

    }

    @Test
    public void TestSchemaValidation() throws JsonParseException {
        SchemaValidator validator = SchemaValidator.getInstance();
        String validJson = "{\"description\":\"example description\",\"extents\":{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[111.111,111.111],[111.111,111.111],[111.111,111.111]]]},\"properties\":null}]}}";
        ProcessingReport report = validator.Llc1(validJson);

        assertThat(report, is(not(nullValue())));
        assertThat(report.isSuccess(), is(true));
    }

    @Test
    public void TestSchemaValidationInvalidJson() throws JsonParseException {
        String invalidJson = "{\"Hello\":\"world\"}";
        ProcessingReport report = getInvalidResult(invalidJson);

        assertThat(report, is(not(nullValue())));
        assertThat(report.isSuccess(), is(false));
        assertThat(report.toString(), containsStringIgnoringCase("object has missing required properties ([\"extents\"]"));
    }

    @Test
    public void TestSchemaValidationInvalidJsonLine() throws JsonParseException {
        String invalidJsonLine = "{\"description\":\"example description\",\"extents\":{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Line\",\"coordinates\":[[111.111,111.111],[111.111,111.111]]},\"properties\":null}]}}";
        ProcessingReport report = getInvalidResult(invalidJsonLine);

        assertThat(report, is(not(nullValue())));
        assertThat(report.isSuccess(), is(false));
        assertThat(report.toString(), containsStringIgnoringCase("instance failed to match exactly one schema"));
    }

    @Test
    public void TestSchemaValidationInvalidJsonPoint() throws JsonParseException {
        String invalidJsonPoint = "{\"description\":\"example description\",\"extents\":{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[111.111,111.111]},\"properties\":null}]}}";
        ProcessingReport report = getInvalidResult(invalidJsonPoint);

        assertThat(report, is(not(nullValue())));
        assertThat(report.isSuccess(), is(false));
        assertThat(report.toString(), containsStringIgnoringCase("instance failed to match exactly one schema"));
    }

    @Test(expected = JsonParseException.class)
    public void TestSchemaValidationInvalidInputJson() throws JsonParseException {
        SchemaValidator validator = SchemaValidator.getInstance();
        validator.Llc1("abc");
    }

    private ProcessingReport getInvalidResult(String json) throws JsonParseException {
        SchemaValidator validator = SchemaValidator.getInstance();
        return validator.Llc1(json);
    }
}
