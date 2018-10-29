package pdfgenerationapi.views.v1_0;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.mashape.unirest.http.Unirest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pdfgenerationapi.ApplicationException;
import pdfgenerationapi.models.Llc1PdfRequest;
import pdfgenerationapi.services.LLC1.IPdfGenerationService;
import pdfgenerationapi.utils.PdfGenerationServiceFactory;
import pdfgenerationapi.utils.SchemaValidator;
import pdfgenerationapi.views.General;
import pdfgenerationapi.services.AuthenticationService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.post;

public class Llc1controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(General.class);

    public void registerRoutes() {
        post("/v1.0/llc1", Llc1controller.generate);
        LOGGER.info("LLC1 routes registered");
    }

    public static Route generate = (Request request, Response response) -> {
        LOGGER.info("Generating PDF for LLC1");
        try{
        	String authHeader = request.headers("Authorization");
        	AuthenticationService.validate(authHeader);
        	Unirest.setDefaultHeader("Authorization", authHeader);
            ProcessingReport report = SchemaValidator.getInstance().Llc1(request.body());
            if(!report.isSuccess()) {
                response.status(400);
                response.type("application/json");
                List<JsonNode> result = new ArrayList<>();
                for (ProcessingMessage message : report) {
                    result.add(message.asJson());
                }
                return result;
            }

            ObjectMapper mapper = new ObjectMapper();
            Llc1PdfRequest llc1PdfRequest = mapper.readValue(request.body(),Llc1PdfRequest.class);
            IPdfGenerationService llc1GenerationService = PdfGenerationServiceFactory.getPdfGenerationInstance("llc1");
            response.status(201);
            response.type("application/json");
            return mapper.writeValueAsString(llc1GenerationService.generatePDF(llc1PdfRequest));
        }
        catch (ApplicationException ex) {
            throw ex;
        }
        catch (JsonParseException ex) {
            response.status(400);
            response.type("application/json");
            return "[{\"level\" : \"error\", \"message\" : \"" + ex.getOriginalMessage() + "\"}]";
        }
        catch (Exception e) {
            LOGGER.error("Error generating LLC1 PDF",e);
            throw new ApplicationException("Error generating LLC1 PDF", "LLC1-01", 500);
        }
    };
}
