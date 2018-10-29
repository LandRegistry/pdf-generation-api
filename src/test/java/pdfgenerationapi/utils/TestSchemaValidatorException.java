package pdfgenerationapi.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.ApplicationException;

import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JsonSchemaFactory.class, JsonLoader.class})
public class TestSchemaValidatorException extends EasyMockSupport {

    @Before
    public void initialize() throws Exception {
        PowerMock.mockStatic(JsonSchemaFactory.class);
        PowerMock.mockStatic(JsonLoader.class);
    }

    @After
    public void after() {
        super.resetAll();
        PowerMock.resetAll();
    }


    @Override
    public void verifyAll() {
        super.verifyAll();
        PowerMock.verifyAll();
    }

    @Override
    public void replayAll() {
        super.replayAll();
        PowerMock.replayAll();
    }

    @Test(expected = ApplicationException.class)
    public void testExceptionGettingInstance() throws IOException {
        JsonSchemaFactory jsonSchemaFactory = mock(JsonSchemaFactory.class);

        EasyMock.expect(JsonSchemaFactory.byDefault()).andReturn(jsonSchemaFactory);
        EasyMock.expect(JsonLoader.fromResource(EasyMock.anyString())).andThrow(new IOException("abc"));

        replayAll();
        SchemaValidator.getInstance();
    }
 
    @Test(expected = ApplicationException.class)
    public void testExceptionProcessingJson() throws ProcessingException, IOException {
        JsonSchemaFactory jsonSchemaFactory = mock(JsonSchemaFactory.class);
        JsonNode jsonNode = mock(JsonNode.class);
        JsonSchema jsonSchema = mock(JsonSchema.class);

        EasyMock.expect(jsonSchemaFactory.getJsonSchema(jsonNode)).andReturn(jsonSchema);
        EasyMock.expect(JsonSchemaFactory.byDefault()).andReturn(jsonSchemaFactory);
        EasyMock.expect(JsonLoader.fromResource(EasyMock.anyString())).andReturn(jsonNode);
        EasyMock.expect(JsonLoader.fromString(EasyMock.anyString())).andReturn(jsonNode);
        EasyMock.expect(jsonSchema.validate(EasyMock.anyObject())).andThrow(new ProcessingException("test"));

        replayAll();
        SchemaValidator.getInstance().Llc1("test");
    }
}
