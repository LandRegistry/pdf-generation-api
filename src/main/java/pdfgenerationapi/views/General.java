package pdfgenerationapi.views;

import static spark.Spark.get;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Route;
import pdfgenerationapi.utils.JsonTransformer;
import pdfgenerationapi.views.logic.HealthWorker;
import pdfgenerationapi.views.logic.CascadeHealthWorker;

public class General {
	private static final Logger LOGGER = LoggerFactory.getLogger(General.class);

	/**
	 * Add all routes, the methods that implement them and body transformer
	 * classes here.
	 */
	public void registerRoutes() {
		get("/health", doHealth, JsonTransformer::render);
		get("/health/cascade/:str_depth", doHealth, JsonTransformer::render);
		LOGGER.info("General routes registered");
	}

	/**
	 * The health route.
	 */
	public Route doHealth = (Request request, Response response) -> {
		// Do the work and return the final body object from the GenericResponse
		if (request.params(":str_depth") == null) {
			return new HealthWorker()
				.process(null, request.params(), request.queryMap().toMap(), request.attribute("headers"))
				.getResult(request, response);
		} else {
			return new CascadeHealthWorker()
				.process(null, request.params(), request.queryMap().toMap(), request.attribute("headers"))
				.getResult(request, response);			
		}
	};
}