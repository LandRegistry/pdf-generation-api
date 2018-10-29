package pdfgenerationapi.views.logic;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.Config;
import pdfgenerationapi.TestUtils;
import pdfgenerationapi.utils.GenericResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Config.class, Unirest.class})

public class TestCascadeHealthWorker extends EasyMockSupport {
    private static final String SERVICE_API_URL = "http://an-api/";
    private HttpResponse mockResponse;
    private GetRequest mockRequest;
    private Headers mockHeaders;

    @BeforeClass
    public static void suitSetup() throws Exception {
        TestUtils.SetupEnvironment();
    }

    @Before
    public void initialize() throws Exception {
        Config.DEPENDENCIES.remove("AnAPI");
        Config.DEPENDENCIES.remove("AnotherAPI");
        Config.DEPENDENCIES.remove("authentication-api");

        mockResponse = mock(HttpResponse.class);
        mockRequest = mock(GetRequest.class);
        mockHeaders = mock(Headers.class);

        PowerMock.mockStatic(Unirest.class); // Needs PrepareForTest annotation at top of class
    }

    @After
    public void after() {
        resetAll();
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

    @Test
    public void testNoHeaders() {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(":str_depth", "1");

        CascadeHealthWorker theWorker = new CascadeHealthWorker();
        GenericResponse<Map<String, Object>> theResponse = theWorker.process(null, urlParams, null, null);

        assertEquals(200, theResponse.httpCode);
        assertEquals("application/json", theResponse.responseBodyContentType);

        assertNotNull("No response body", theResponse.responseBodyObject);
        assertEquals("Incorrect number of fields in response", 8, theResponse.responseBodyObject.size());
        assertEquals("Incorrent number of reflected headers", 0, ((List<List<String>>) theResponse.responseBodyObject.get("headers")).size());
        assertEquals("Incorrect app name", "pdf-generation-api", theResponse.responseBodyObject.get("app"));
        assertEquals("Incorrect commit", "LOCAL", theResponse.responseBodyObject.get("commit"));
        assertEquals("Incorrect status", "OK", theResponse.responseBodyObject.get("status"));
    }

    @Test
    public void testWithHeader() {
        Map<String, String> headers = new HashMap<>(1);
        headers.put("MyHeader", "MyValue");

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(":str_depth", "1");

        CascadeHealthWorker theWorker = new CascadeHealthWorker();
        GenericResponse<Map<String, Object>> theResponse = theWorker.process(null, urlParams, null, headers);

        assertEquals(200, theResponse.httpCode);
        assertEquals("application/json", theResponse.responseBodyContentType);

        assertNotNull("No response body", theResponse.responseBodyObject);
        assertEquals("Incorrect number of fields in response", 8, theResponse.responseBodyObject.size());

        assertThat("Headers should be a list", theResponse.responseBodyObject.get("headers"), instanceOf(List.class));
        List<List<String>> outHeaders = (List<List<String>>)theResponse.responseBodyObject.get("headers");
        assertEquals("Incorrect number of reflected headers", 1, outHeaders.size());
        assertEquals("Incorrect reflected header key", "MyHeader", outHeaders.get(0).get(0));
        assertEquals("Incorrect reflected header value", "MyValue", outHeaders.get(0).get(1));

        assertEquals("Incorrect app name", "pdf-generation-api", theResponse.responseBodyObject.get("app"));
        assertEquals("Incorrect commit", "LOCAL", theResponse.responseBodyObject.get("commit"));
        assertEquals("Incorrect status", "OK", theResponse.responseBodyObject.get("status"));
    }

    @Test
    public void testDepthOutOfRange() {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(":str_depth", "100");

        CascadeHealthWorker theWorker = new CascadeHealthWorker();
        GenericResponse<Map<String, Object>> theResponse = theWorker.process(null, urlParams, null, null);

        assertEquals(400, theResponse.httpCode);
        assertEquals("application/json", theResponse.responseBodyContentType);

        assertNotNull("No response body", theResponse.responseBodyObject);
        assertEquals("Incorrect number of fields in response", 4, theResponse.responseBodyObject.size());
        assertEquals("Incorrect app name", "pdf-generation-api", theResponse.responseBodyObject.get("app"));
        assertEquals("Incorrect cascade depth", 100, theResponse.responseBodyObject.get("cascade_depth"));
        assertEquals("Incorrect status", "ERROR", theResponse.responseBodyObject.get("status"));
    }

    @Test
    public void testDepthOutOfLowerRange() {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(":str_depth", "-1");

        CascadeHealthWorker theWorker = new CascadeHealthWorker();
        GenericResponse<Map<String, Object>> theResponse = theWorker.process(null, urlParams, null, null);

        assertEquals(400, theResponse.httpCode);
        assertEquals("application/json", theResponse.responseBodyContentType);

        assertNotNull("No response body", theResponse.responseBodyObject);
        assertEquals("Incorrect number of fields in response", 4, theResponse.responseBodyObject.size());
        assertEquals("Incorrect app name", "pdf-generation-api", theResponse.responseBodyObject.get("app"));
        assertEquals("Incorrect cascade depth", -1, theResponse.responseBodyObject.get("cascade_depth"));
        assertEquals("Incorrect status", "ERROR", theResponse.responseBodyObject.get("status"));
    }

    @Test
    public void testServiceOK() throws UnirestException {
        Config.DEPENDENCIES.put("AnAPI", SERVICE_API_URL);
        Config.DEPENDENCIES.put("AnotherAPI", "http://another-api");

        EasyMock.expect(Unirest.get(EasyMock.anyString())).andReturn(mockRequest).times(2);
        EasyMock.expect(mockRequest.header(EasyMock.anyString(), EasyMock.anyString())).andReturn(mockRequest).times(2);
        EasyMock.expect(mockRequest.asString()).andReturn(mockResponse).times(2);
        EasyMock.expect(mockResponse.getStatus()).andReturn(200).times(2);
        EasyMock.expect(mockResponse.getHeaders()).andReturn(mockHeaders).times(2);
        EasyMock.expect(mockHeaders.getFirst(EasyMock.anyString())).andReturn("myContentType").times(2);
        EasyMock.expect(mockResponse.getBody()).andReturn("{\"name\" : \"value\"}").times(2);

        replayAll();

        CascadeHealthWorker theWorker = new CascadeHealthWorker();

        Map<String, String> urlParams = new HashMap<String, String>(1);
        urlParams.put(":str_depth", "1");
        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("X-Trace-ID", "123");

        GenericResponse<Map<String, Object>> theResponse = theWorker.process(null, urlParams, null, headers);

        assertEquals("Overall status code not correct", 200, theResponse.httpCode);

        List<Map<String, Object>> services = (List<Map<String, Object>>)theResponse.responseBodyObject.get("services");
        assertEquals("Service information not in response", 2, services.size());

        Map<String, Object> service = services.get(0);
        assertEquals("Incorrect number of service fields returned", 6, service.size());

        assertEquals("Service name not correct", "AnotherAPI", service.get("name"));
        assertEquals("Service type not correct", "http", service.get("type"));
        assertEquals("Service status code not correct", 200, service.get("status_code"));
        assertEquals("Service content type not correct", "myContentType", service.get("content_type"));
        assertEquals("Service status not correct", "OK", service.get("status"));
        Map<String, Object> serviceContent = (Map<String, Object>)service.get("content");
        assertEquals("Service response content not in response", 1, serviceContent.size());
        assertEquals("Service response content not correct", "value", serviceContent.get("name"));

        service = services.get(1);
        assertEquals("Incorrect number of service fields returned", 6, service.size());

        assertEquals("Service name not correct", "AnAPI", service.get("name"));
        assertEquals("Service type not correct", "http", service.get("type"));
        assertEquals("Service status code not correct", 200, service.get("status_code"));
        assertEquals("Service content type not correct", "myContentType", service.get("content_type"));
        assertEquals("Service status not correct", "OK", service.get("status"));
        serviceContent = (Map<String, Object>)service.get("content");
        assertEquals("Service response content not in response", 1, serviceContent.size());
        assertEquals("Service response content not correct", "value", serviceContent.get("name"));

        verifyAll();
        after();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDBUnsupported() throws UnirestException {
        // Prepare the environment
        Config.DEPENDENCIES.put("AnAPI", SERVICE_API_URL);
        Config.DEPENDENCIES.put("AnotherAPI", "jdbc:another-api");

        // Set up mock expectations
        EasyMock.expect(Unirest.get(EasyMock.anyString())).andReturn(mockRequest).times(2);
        // Expect the rest of the unirest chain. Will test contents in other tests
        EasyMock.expect(mockRequest.header(EasyMock.anyString(), EasyMock.anyString())).andReturn(mockRequest).times(2);
        EasyMock.expect(mockRequest.asString()).andReturn(mockResponse).times(2);
        EasyMock.expect(mockResponse.getStatus()).andReturn(200).times(2);
        EasyMock.expect(mockResponse.getHeaders()).andReturn(mockHeaders).times(2);
        EasyMock.expect(mockHeaders.getFirst(EasyMock.anyString())).andReturn("myContentType").times(2);
        EasyMock.expect(mockResponse.getBody()).andReturn("{\"name\" : \"value\"}").times(2);

        replayAll();

        CascadeHealthWorker theWorker = new CascadeHealthWorker();

        Map<String, String> urlParams = new HashMap<String, String>(1);
        urlParams.put(":str_depth", "1");
        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("X-Trace-ID", "123");

        GenericResponse<Map<String, Object>> theResponse = theWorker.process(null, urlParams, null, headers);
    }

    @Test
    public void testService500() throws UnirestException {
        Config.DEPENDENCIES.put("AnAPI", SERVICE_API_URL);

        EasyMock.expect(Unirest.get(EasyMock.anyString())).andReturn(mockRequest).atLeastOnce();
        EasyMock.expect(mockRequest.header(EasyMock.anyString(), EasyMock.anyString())).andReturn(mockRequest).atLeastOnce();
        EasyMock.expect(mockRequest.asString()).andReturn(mockResponse).atLeastOnce();
        EasyMock.expect(mockResponse.getStatus()).andReturn(500).atLeastOnce();
        EasyMock.expect(mockResponse.getHeaders()).andReturn(mockHeaders).atLeastOnce();
        EasyMock.expect(mockHeaders.getFirst(EasyMock.anyString())).andReturn("myContentType").atLeastOnce();

        replayAll();

        CascadeHealthWorker theWorker = new CascadeHealthWorker();

        Map<String, String> urlParams = new HashMap<>(1);
        urlParams.put(":str_depth", "1");
        Map<String, String> headers = new HashMap<>(1);
        headers.put("X-Trace-ID", "123");

        GenericResponse<Map<String, Object>> theResponse = theWorker.process(null, urlParams, null, headers);

        assertEquals("Overall status code not correct", 500, theResponse.httpCode);

        List<Map<String, Object>> services = (List<Map<String, Object>>)theResponse.responseBodyObject.get("services");
        assertEquals("Service information not in response", 1, services.size());

        Map<String, Object> service = services.get(0);
        assertEquals("Incorrect number of service fields returned", 5, service.size());

        assertEquals("Service name not correct", "AnAPI", service.get("name"));
        assertEquals("Service type not correct", "http", service.get("type"));
        assertEquals("Service status code not correct", 500, service.get("status_code"));
        assertEquals("Service content type not correct", "myContentType", service.get("content_type"));
        assertEquals("Service status not correct", "BAD", service.get("status"));

        verifyAll();
    }

    @Test
    public void testUnirestException() throws UnirestException {
        Config.DEPENDENCIES.put("AnAPI", SERVICE_API_URL);

        EasyMock.expect(Unirest.get(EasyMock.anyString())).andReturn(mockRequest).atLeastOnce();
        EasyMock.expect(mockRequest.header(EasyMock.anyString(), EasyMock.anyString())).andReturn(mockRequest).atLeastOnce();
        EasyMock.expect(mockRequest.asString()).andThrow(new UnirestException("error"));

        replayAll();

        CascadeHealthWorker theWorker = new CascadeHealthWorker();

        Map<String, String> urlParams = new HashMap<>(1);
        urlParams.put(":str_depth", "1");
        Map<String, String> headers = new HashMap<>(1);
        headers.put("X-Trace-ID", "123");

        GenericResponse<Map<String, Object>> theResponse = theWorker.process(null, urlParams, null, headers);

        assertEquals("Overall status code not correct", 500, theResponse.httpCode);

        List<Map<String, Object>> services = (List<Map<String, Object>>)theResponse.responseBodyObject.get("services");
        assertEquals("Service information not in response", 1, services.size());

        Map<String, Object> service = services.get(0);
        assertEquals("Incorrect number of service fields returned", 6, service.size());

        assertEquals("Service name not correct", "AnAPI", service.get("name"));
        assertEquals("Service type not correct", "http", service.get("type"));
        assertEquals("Service status code not correct", null, service.get("status_code"));
        assertEquals("Service content type not correct", null, service.get("content_type"));
        assertEquals("Service content not correct", null, service.get("content"));
        assertEquals("Service status not correct", "UNKNOWN", service.get("status"));

        verifyAll();
    }

    @Test
    public void testService400() throws UnirestException {
        Config.DEPENDENCIES.put("AnAPI", SERVICE_API_URL);

        EasyMock.expect(Unirest.get(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.header(EasyMock.anyString(), EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.asString()).andReturn(mockResponse);
        EasyMock.expect(mockResponse.getStatus()).andReturn(400);
        EasyMock.expect(mockResponse.getHeaders()).andReturn(mockHeaders);
        EasyMock.expect(mockHeaders.getFirst(EasyMock.anyString())).andReturn("myContentType");

        replayAll();

        CascadeHealthWorker theWorker = new CascadeHealthWorker();

        Map<String, String> urlParams = new HashMap<>(1);
        urlParams.put(":str_depth", "1");
        Map<String, String> headers = new HashMap<>(1);
        headers.put("X-Trace-ID", "123");

        GenericResponse<Map<String, Object>> theResponse = theWorker.process(null, urlParams, null, headers);

        assertEquals("Overall status code not correct", 500, theResponse.httpCode);

        List<Map<String, Object>> services = (List<Map<String, Object>>)theResponse.responseBodyObject.get("services");
        assertEquals("Service information not in response", 1, services.size());

        Map<String, Object> service = services.get(0);
        assertEquals("Incorrect number of service fields returned", 5, service.size());

        assertEquals("Service name not correct", "AnAPI", service.get("name"));
        assertEquals("Service type not correct", "http", service.get("type"));
        assertEquals("Service status code not correct", 400, service.get("status_code"));
        assertEquals("Service content type not correct", "myContentType", service.get("content_type"));
        assertEquals("Service status not correct", "UNKNOWN", service.get("status"));

        verifyAll();
    }
}
