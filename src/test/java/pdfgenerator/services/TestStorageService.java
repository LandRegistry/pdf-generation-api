package pdfgenerator.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;
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
import pdfgenerator.exceptions.StorageException;
import pdfgenerator.models.StorageResult;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Config.class, Unirest.class})
public class TestStorageService extends EasyMockSupport {

    private HttpResponse<JsonNode> mockResponse;
    private HttpRequestWithBody mockRequest;

    @BeforeClass
    public static void suitSetup() throws Exception {
        TestUtils.SetupEnvironment();
    }

    @Before
    public void initialize() throws Exception {
        mockResponse = mock(HttpResponse.class);
        mockRequest = mock(HttpRequestWithBody.class);

        PowerMock.mockStatic(Unirest.class);
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

    @Test
    public void testSuccessfulResult() throws UnirestException, StorageException, IOException {
        JsonNode jsonResponse = new JsonNode("{\"file\": [{\"bucket\":\"abc\",\"file_id\":\"123\", \"external_reference\" : \"external-link\"}]}");
        MultipartBody body = mock(MultipartBody.class);
        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.field(EasyMock.anyString(), EasyMock.anyObject(File.class), EasyMock.anyString())).andReturn(body);
        EasyMock.expect(body.asJson()).andReturn(mockResponse);
        EasyMock.expect(mockResponse.getStatus()).andReturn(201).atLeastOnce();
        EasyMock.expect(mockResponse.getBody()).andReturn(jsonResponse).atLeastOnce();

        replayAll();

        File file = mock(File.class);

        StorageResult result = StorageService.save(file);

        assertThat(result, is(not(nullValue())));
        assertThat(result.getDocumentUrl(), containsStringIgnoringCase("/abc"));
        assertThat(result.getDocumentUrl(), containsStringIgnoringCase("/123"));
        assertThat(result.getExternalUrl(), is("external-link"));
    }

    @Test(expected = StorageException.class)
    public void test400Response() throws UnirestException, StorageException, IOException {
        MultipartBody body = mock(MultipartBody.class);
        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.field(EasyMock.anyString(), EasyMock.anyObject(File.class), EasyMock.anyString())).andReturn(body);
        EasyMock.expect(body.asJson()).andReturn(mockResponse);
        EasyMock.expect(mockResponse.getStatus()).andReturn(400).atLeastOnce();

        replayAll();

        File file = mock(File.class);

        StorageService.save(file);
    }

    @Test(expected = StorageException.class)
    public void test500Response() throws UnirestException, StorageException, IOException {
        MultipartBody body = mock(MultipartBody.class);
        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.field(EasyMock.anyString(), EasyMock.anyObject(File.class), EasyMock.anyString())).andReturn(body);
        EasyMock.expect(body.asJson()).andReturn(mockResponse);
        EasyMock.expect(mockResponse.getStatus()).andReturn(500).atLeastOnce();

        replayAll();

        File file = mock(File.class);

        StorageService.save(file);
    }

    @Test(expected = StorageException.class)
    public void testUnirestException() throws UnirestException, StorageException, IOException {
        MultipartBody body = mock(MultipartBody.class);
        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.field(EasyMock.anyString(), EasyMock.anyObject(File.class), EasyMock.anyString())).andReturn(body);
        EasyMock.expect(body.asJson()).andThrow(new UnirestException("error"));

        replayAll();

        File file = mock(File.class);

        StorageService.save(file);
    }
}
