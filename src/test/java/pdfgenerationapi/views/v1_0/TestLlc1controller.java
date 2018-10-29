package pdfgenerationapi.views.v1_0;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.mashape.unirest.http.Unirest;

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
import pdfgenerationapi.models.Llc1GenerationResult;
import pdfgenerationapi.services.LLC1.IPdfGenerationService;
import pdfgenerationapi.services.LLC1.llc1PdfGenerationService;
import pdfgenerationapi.utils.PdfGenerationServiceFactory;
import pdfgenerationapi.utils.SchemaValidator;
import pdfgenerator.models.LocalLandCharge;
import pdfgenerationapi.services.AuthenticationService;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SchemaValidator.class, PdfGenerationServiceFactory.class, Spark.class, AuthenticationService.class, Unirest.class})
public class TestLlc1controller extends EasyMockSupport {

    private final String validJson = "{\"description\":\"example description\",\"extents\":{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[111.111,111.111],[111.111,111.111],[111.111,111.111]]]},\"properties\":null}]}}";


    @Before
    public void initialize() throws Exception {

        PowerMock.mockStatic(SchemaValidator.class);
        PowerMock.mockStatic(PdfGenerationServiceFactory.class);
        PowerMock.mockStatic(Spark.class);
        PowerMock.mockStatic(AuthenticationService.class);
        PowerMock.mockStatic(Unirest.class);
    }

    @After
    public void after() {
        // Remove all expectations from mocks and put them back into record mode
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
    public void TestSuccessfulRequest() throws Exception {
        ProcessingReport report = mock(ProcessingReport.class);
        SchemaValidator validator = mock(SchemaValidator.class);
        List<LocalLandCharge> includedCharges =  new ArrayList<LocalLandCharge>();
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        Llc1GenerationResult llc1GenerationResult = new Llc1GenerationResult("abc", "external-link", includedCharges);
        IPdfGenerationService pdfGenerationService = mock(llc1PdfGenerationService.class);

        EasyMock.expect(request.body()).andReturn(validJson).times(2);
        EasyMock.expect(request.headers("Authorization")).andReturn("Bearer NOTREALAJWT");
        response.status(201);
        EasyMock.expectLastCall();
        response.type("application/json");
        EasyMock.expectLastCall();
        EasyMock.expect(report.isSuccess()).andReturn(true);
        EasyMock.expect(validator.Llc1(EasyMock.anyString())).andReturn(report);
        EasyMock.expect(SchemaValidator.getInstance()).andReturn(validator);
        EasyMock.expect(pdfGenerationService.generatePDF(EasyMock.anyObject())).andReturn(llc1GenerationResult).atLeastOnce();
        EasyMock.expect(PdfGenerationServiceFactory.getPdfGenerationInstance(EasyMock.anyString())).andReturn(pdfGenerationService);
        EasyMock.expect(AuthenticationService.validate(EasyMock.anyString())).andReturn(true);
        Unirest.setDefaultHeader("Authorization", "Bearer NOTREALAJWT");
        EasyMock.expectLastCall().once();

        replayAll();

        Route route = Llc1controller.generate;
        Object result = route.handle(request,response);

        assertThat(result, is(not(nullValue())));
        assertThat(result, is("{\"included_charges\":[],\"document_url\":\"abc\",\"external_url\":\"external-link\"}"));
    }

    @Test
    public void TestBadRequestFromUser() throws Exception {
        ProcessingReport report = mock(ProcessingReport.class);
        SchemaValidator validator = mock(SchemaValidator.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        ProcessingMessage processingMessage = new ProcessingMessage();
        processingMessage.put("abc","def");
        ProcessingMessage[] processingMessages = new ProcessingMessage[]{processingMessage};
        Iterator<ProcessingMessage> iterator = new ArrayIterator<>(processingMessages);

        EasyMock.expect(request.body()).andReturn(validJson).times(2);
        EasyMock.expect(request.headers("Authorization")).andReturn("Bearer NOTREALAJWT");
        response.status(400);
        EasyMock.expectLastCall();
        response.type("application/json");
        EasyMock.expectLastCall();
        EasyMock.expect(report.isSuccess()).andReturn(false);
        EasyMock.expect(report.iterator()).andReturn(iterator);
        EasyMock.expect(validator.Llc1(EasyMock.anyString())).andReturn(report);
        EasyMock.expect(SchemaValidator.getInstance()).andReturn(validator);
        EasyMock.expect(AuthenticationService.validate(EasyMock.anyString())).andReturn(true);
        Unirest.setDefaultHeader("Authorization", "Bearer NOTREALAJWT");
        EasyMock.expectLastCall().once();

        replayAll();

        Route route = Llc1controller.generate;
        ArrayList<JsonNode> result = (ArrayList<JsonNode>)route.handle(request,response);

        assertThat(result, is(not(nullValue())));
        assertThat(result.get(0).toString(), is("{\"level\":\"info\",\"abc\":\"def\"}"));
    }

    @Test(expected = ApplicationException.class)
    public void TestApplicationException() throws Exception {

        ProcessingReport report = mock(ProcessingReport.class);
        SchemaValidator validator = mock(SchemaValidator.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);


        EasyMock.expect(request.body()).andReturn(validJson).times(2);
        EasyMock.expect(request.headers("Authorization")).andReturn("Bearer NOTREALAJWT");
        response.status(500);
        EasyMock.expectLastCall();
        response.type("application/json");
        EasyMock.expectLastCall();
        EasyMock.expect(report.isSuccess()).andThrow(new ApplicationException("An Error happened","123",500));
        EasyMock.expect(validator.Llc1(EasyMock.anyString())).andReturn(report);
        EasyMock.expect(SchemaValidator.getInstance()).andReturn(validator);
        EasyMock.expect(AuthenticationService.validate(EasyMock.anyString())).andReturn(true);
        Unirest.setDefaultHeader("Authorization", "Bearer NOTREALAJWT");
        EasyMock.expectLastCall().once();

        replayAll();

        Route route = Llc1controller.generate;
        route.handle(request,response);

    }

    @Test(expected = ApplicationException.class)
    public void TestException() throws Exception {

        ProcessingReport report = mock(ProcessingReport.class);
        SchemaValidator validator = mock(SchemaValidator.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        EasyMock.expect(request.body()).andReturn(validJson).times(2);
        EasyMock.expect(request.headers("Authorization")).andReturn("Bearer NOTREALAJWT");
        response.status(500);
        EasyMock.expectLastCall();
        response.type("application/json");
        EasyMock.expectLastCall();
        EasyMock.expect(report.isSuccess()).andThrow(new IllegalArgumentException());
        EasyMock.expect(validator.Llc1(EasyMock.anyString())).andReturn(report);
        EasyMock.expect(SchemaValidator.getInstance()).andReturn(validator);
        EasyMock.expect(AuthenticationService.validate(EasyMock.anyString())).andReturn(true);
        Unirest.setDefaultHeader("Authorization", "Bearer NOTREALAJWT");
        EasyMock.expectLastCall().once();

        replayAll();

        Route route = Llc1controller.generate;
        route.handle(request,response);

    }

    @Test
    public void TestJsonParseException() throws Exception {

        SchemaValidator validator = mock(SchemaValidator.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        EasyMock.expect(request.body()).andReturn(validJson).times(2);
        EasyMock.expect(request.headers("Authorization")).andReturn("Bearer NOTREALAJWT");
        response.status(400);
        EasyMock.expectLastCall();
        response.type("application/json");
        EasyMock.expectLastCall();
        EasyMock.expect(validator.Llc1(EasyMock.anyString())).andThrow(new JsonParseException("abc",null));
        EasyMock.expect(SchemaValidator.getInstance()).andReturn(validator);
        EasyMock.expect(AuthenticationService.validate(EasyMock.anyString())).andReturn(true);
        Unirest.setDefaultHeader("Authorization", "Bearer NOTREALAJWT");
        EasyMock.expectLastCall().once();

        replayAll();

        Route route = Llc1controller.generate;
        String result = (String)route.handle(request,response);

        assertThat(result, is(not(nullValue())));
        assertThat(result, is("[{\"level\" : \"error\", \"message\" : \"abc\"}]"));

    }


    @Test()
    public void TestRegisterRoutes(){
        Spark.post("/v1.0/llc1", Llc1controller.generate);
        EasyMock.expectLastCall();
        replayAll();

        Llc1controller llc1controller = new Llc1controller();
        llc1controller.registerRoutes();
        assertThat(llc1controller, is(not(nullValue())));
    }
}
