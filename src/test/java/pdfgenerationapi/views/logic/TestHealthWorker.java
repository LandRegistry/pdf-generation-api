package pdfgenerationapi.views.logic;

import org.junit.Before;
import org.junit.Test;
import pdfgenerationapi.TestUtils;
import pdfgenerationapi.utils.GenericResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class TestHealthWorker {

    @Before
    public void initialize() throws Exception {
        // Prepare the environment for Config
        TestUtils.SetupEnvironment();
    }

    @Test
    public void testNoHeaders() {
        HealthWorker theWorker = new HealthWorker();
        GenericResponse<Map<String, Object>> theResponse = theWorker.process(null, null, null, null);

        // Check OK response
        assertEquals(200, theResponse.httpCode);
        assertEquals("application/json", theResponse.responseBodyContentType);

        // Check body is as expected
        assertNotNull("No response body", theResponse.responseBodyObject);
        assertEquals("Incorrect number of fields in response", 4, theResponse.responseBodyObject.size());
        assertEquals("Incorrent number of reflected headers", 0, ((List<List<String>>) theResponse.responseBodyObject.get("headers")).size());
        assertEquals("Incorrect app name", "pdf-generation-api", theResponse.responseBodyObject.get("app"));
        assertEquals("Incorrect commit", "LOCAL", theResponse.responseBodyObject.get("commit"));
        assertEquals("Incorrect status", "OK", theResponse.responseBodyObject.get("status"));
    }

    @Test
    public void testWithHeader() {
        // Prepare input
        Map<String, String> headers = new HashMap<>(1);
        headers.put("MyHeader", "MyValue");

        // Do it
        HealthWorker theWorker = new HealthWorker();
        GenericResponse<Map<String, Object>> theResponse = theWorker.process(null, null, null, headers);

        // Check OK response
        assertEquals(200, theResponse.httpCode);
        assertEquals("application/json", theResponse.responseBodyContentType);

        // Check body is as expected
        assertNotNull("No response body", theResponse.responseBodyObject);
        assertEquals("Incorrect number of fields in response", 4, theResponse.responseBodyObject.size());

        assertThat("Headers should be a list", theResponse.responseBodyObject.get("headers"), instanceOf(List.class));
        List<List<String>> outHeaders = (List<List<String>>)theResponse.responseBodyObject.get("headers");
        assertEquals("Incorrect number of reflected headers", 1, outHeaders.size());
        assertEquals("Incorrect reflected header key", "MyHeader", outHeaders.get(0).get(0));
        assertEquals("Incorrect reflected header value", "MyValue", outHeaders.get(0).get(1));

        assertEquals("Incorrect app name", "pdf-generation-api", theResponse.responseBodyObject.get("app"));
        assertEquals("Incorrect commit", "LOCAL", theResponse.responseBodyObject.get("commit"));
        assertEquals("Incorrect status", "OK", theResponse.responseBodyObject.get("status"));
    }
}
