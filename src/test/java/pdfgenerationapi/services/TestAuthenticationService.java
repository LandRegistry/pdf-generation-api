package pdfgenerationapi.services;

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

import pdfgenerationapi.ApplicationException;
import pdfgenerationapi.Config;
import pdfgenerationapi.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Config.class, Unirest.class})
public class TestAuthenticationService extends EasyMockSupport {

    private HttpResponse<JsonNode> mockResponse;
    private HttpRequestWithBody mockRequest;
    private MultipartBody mockBody;

    @BeforeClass
    public static void suitSetup() throws Exception {
        TestUtils.SetupEnvironment();
    }

    @Before
    public void initialize() throws Exception {
        mockResponse = mock(HttpResponse.class);
        mockRequest = mock(HttpRequestWithBody.class);
        mockBody = mock(MultipartBody.class);

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
    public void TestSuccessfulResult() throws UnirestException {
        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.field("token", "NOTAREALJWT")).andReturn(mockBody);
        EasyMock.expect(mockBody.asJson()).andReturn(mockResponse);
        EasyMock.expect(mockResponse.getStatus()).andReturn(200).atLeastOnce();
        replayAll();

        boolean result = AuthenticationService.validate("Bearer NOTAREALJWT");

        assertThat(result, is(true));
    }

    @Test
    public void TestNoBearer() {
    	ApplicationException except = null;    	
    	try {
    		AuthenticationService.validate("Badgerer NOTAREALJWT");
    	}
    	catch (ApplicationException e) {
    		except = e;
    	}
    	assertThat(except.getMessage(), is("Invalid JWT header, expected 'Bearer <token>', received 'Badgerer NOTAREALJWT'"));
    }
    
    @Test
    public void TestNullToken() {
    	ApplicationException except = null;    	
    	try {
    		AuthenticationService.validate(null);
    	}
    	catch (ApplicationException e) {
    		except = e;
    	}
    	assertThat(except.getMessage(), is("Invalid JWT header, expected 'Bearer <token>', received 'null'"));
    }
    
    @Test
    public void TestAuthnFailure() throws UnirestException {
        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.field("token", "NOTAREALJWT")).andReturn(mockBody);
        EasyMock.expect(mockBody.asJson()).andReturn(mockResponse);
        EasyMock.expect(mockResponse.getStatus()).andReturn(401).atLeastOnce();
        replayAll();
    	ApplicationException except = null;    	
    	try {
    		AuthenticationService.validate("Bearer NOTAREALJWT");
    	}
    	catch (ApplicationException e) {
    		except = e;
    	}
    	assertThat(except.getMessage(), is("Call to authentication API returned a failed status code: 401"));
    }
    
    @Test
    public void TestRestExcept() throws UnirestException {
        EasyMock.expect(Unirest.post(EasyMock.anyString())).andReturn(mockRequest);
        EasyMock.expect(mockRequest.field("token", "NOTAREALJWT")).andReturn(mockBody);
        EasyMock.expect(mockBody.asJson()).andThrow(new UnirestException("Wrongness"));
        replayAll();
    	ApplicationException except = null;    	
    	try {
    		AuthenticationService.validate("Bearer NOTAREALJWT");
    	}
    	catch (ApplicationException e) {
    		except = e;
    	}
    	assertThat(except.getMessage(), is("Failed to call authentication API. Exception: Wrongness"));
    }
}