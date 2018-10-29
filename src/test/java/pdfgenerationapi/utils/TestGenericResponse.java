package pdfgenerationapi.utils;

import org.junit.Test;
import spark.Request;
import spark.Response;

import static org.easymock.EasyMock.mock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestGenericResponse {

    @Test
    public void TestGenericResponseConstruction() {
        GenericResponse response = new GenericResponse<>(200,"abc","def");
        assertThat(response.httpCode, is(200));
        assertThat(response.responseBodyContentType, is("abc"));
        assertThat(response.responseBodyObject, is("def"));

    }

    @Test
    public void TestGenericResponseGetResult() {
        GenericResponse<String> genericResponse = new GenericResponse<>(200,"abc","def");

        Response response = mock(Response.class);
        Request request = mock(Request.class);

        String result = genericResponse.getResult(request, response);
        assertThat(result, is("def"));
    }
}
