package pdfgenerator.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
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
import pdfgenerator.exceptions.SearchException;
import pdfgenerator.models.LocalLandCharge;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Config.class, Unirest.class})
public class TestSearchService extends EasyMockSupport {

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
    public void TestSuccessfulResult() throws UnirestException, SearchException, IOException {
        JsonNode jsonResponse = new JsonNode("[{\"item\":{\"further-information-reference\":null,\"registration-date\":null,\"author\":null,\"further-information-location\":\"\",\"start-date\":null,\"instrument\":null,\"charge-geographic-description\":\"\",\"charge-type\":null,\"charge-creation-date\":null,\"originating-authority\":null,\"geometry\":null,\"statutory-provision\":null,\"local-land-charge\":null},\"display_id\":\"abc\",\"cancelled\":false,\"geometry\":{\"type\":null,\"features\":[]},\"id\":1,\"type\":\"type\"}]");

        RequestBodyEntity mockBody = mock(RequestBodyEntity.class);

        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.header("Content-Type", "application/json")).andReturn(mockRequest);
        EasyMock.expect(mockRequest.queryString("filter", "cancelled")).andReturn(mockRequest);
        EasyMock.expect(mockRequest.body(EasyMock.anyString())).andReturn(mockBody);
        EasyMock.expect(mockBody.asJson()).andReturn(mockResponse);
        EasyMock.expect(mockResponse.getStatus()).andReturn(200).atLeastOnce();
        EasyMock.expect(mockResponse.getBody()).andReturn(jsonResponse);

        replayAll();

        List<LocalLandCharge> result = SearchService.get(TestUtils.buildLlcRequest());

        assertThat(result, is(not(nullValue())));
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getDisplayId(), is("abc"));
        assertThat(result.get(0).getId(), is(1));
    }

    @Test
    public void TestNoResultsFound() throws UnirestException, SearchException, IOException {

        RequestBodyEntity mockBody = mock(RequestBodyEntity.class);

        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.header("Content-Type", "application/json")).andReturn(mockRequest);
        EasyMock.expect(mockRequest.queryString("filter", "cancelled")).andReturn(mockRequest);
        EasyMock.expect(mockRequest.body(EasyMock.anyString())).andReturn(mockBody);
        EasyMock.expect(mockBody.asJson()).andReturn(mockResponse);
        EasyMock.expect(mockResponse.getStatus()).andReturn(404).atLeastOnce();

        replayAll();

        List<LocalLandCharge> result = SearchService.get(TestUtils.buildLlcRequest());

        assertThat(result, is(not(nullValue())));
        assertThat(result.size(), is(0));
    }

    @Test(expected = SearchException.class)
    public void TestBadRequest() throws UnirestException, SearchException, IOException {

        RequestBodyEntity mockBody = mock(RequestBodyEntity.class);

        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.header("Content-Type", "application/json")).andReturn(mockRequest);
        EasyMock.expect(mockRequest.queryString("filter", "cancelled")).andReturn(mockRequest);
        EasyMock.expect(mockRequest.body(EasyMock.anyString())).andReturn(mockBody);
        EasyMock.expect(mockBody.asJson()).andReturn(mockResponse);
        EasyMock.expect(mockResponse.getStatus()).andReturn(400).atLeastOnce();

        replayAll();

        SearchService.get(TestUtils.buildLlcRequest());
    }

    @Test(expected = SearchException.class)
    public void TestServiceOffline() throws UnirestException, SearchException, IOException {

        RequestBodyEntity mockBody = mock(RequestBodyEntity.class);

        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.header("Content-Type", "application/json")).andReturn(mockRequest);
        EasyMock.expect(mockRequest.queryString("filter", "cancelled")).andReturn(mockRequest);
        EasyMock.expect(mockRequest.body(EasyMock.anyString())).andReturn(mockBody);
        EasyMock.expect(mockBody.asJson()).andReturn(mockResponse);
        EasyMock.expect(mockResponse.getStatus()).andReturn(500).atLeastOnce();

        replayAll();

        SearchService.get(TestUtils.buildLlcRequest());
    }

    @Test(expected = SearchException.class)
    public void TestUnirestException() throws UnirestException, SearchException, IOException {

        RequestBodyEntity mockBody = mock(RequestBodyEntity.class);

        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.header("Content-Type", "application/json")).andReturn(mockRequest);
        EasyMock.expect(mockRequest.queryString("filter", "cancelled")).andReturn(mockRequest);
        EasyMock.expect(mockRequest.body(EasyMock.anyString())).andReturn(mockBody);
        EasyMock.expect(mockBody.asJson()).andThrow(new UnirestException("error"));

        replayAll();

        SearchService.get(TestUtils.buildLlcRequest());
    }
}