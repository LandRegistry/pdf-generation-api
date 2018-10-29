package pdfgenerationapi.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pdfgenerationapi.ApplicationException;

public class SchemaValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaValidator.class);

    private static SchemaValidator instance;
    private final JsonSchema llc1_schema;

    private SchemaValidator() {
        try {

            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonNode llc1schema = JsonLoader.fromResource("/llc1-request-schema.json");
            llc1_schema = factory.getJsonSchema(llc1schema);
        }
        catch (Exception e){
            LOGGER.error("Error with schema validation", e);
            throw new ApplicationException("Error setting up schema validation","schema-01",500);
        }
    }

    public static synchronized SchemaValidator getInstance() {
        if(instance == null){
            instance = new SchemaValidator();
        }
        return instance;
    }


    public ProcessingReport Llc1(String json) throws JsonParseException {
        try {
            return llc1_schema.validate(JsonLoader.fromString(json));
        }
        catch (JsonParseException ex) {
            throw ex;
        }
        catch (Exception e) {
            LOGGER.error("Error with schema validation", e);
            throw new ApplicationException("Error processing schema validation","schema-03",500);
        }

    }

}
