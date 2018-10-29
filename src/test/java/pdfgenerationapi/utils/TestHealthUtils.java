package pdfgenerationapi.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestHealthUtils {

    @Test
    public void testConstructor() {
        HealthUtils healthUtils = new HealthUtils();
        assertThat(healthUtils, is(not(nullValue())));
    }

    @Test
    public void testExtractHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("a","1");
        headers.put("b", "2");

        List<List<String>> result = HealthUtils.extractHeaders(headers);

        assertThat(result.size(), is(2));
        assertThat(result.get(0).size(), is(2));
        assertThat(result.get(0).get(0) ,is("a"));
        assertThat(result.get(0).get(1) ,is("1"));
        assertThat(result.get(1).get(0) ,is("b"));
        assertThat(result.get(1).get(1) ,is("2"));
    }

    @Test
    public void testExtractHeadersWithEmptyList() {
        Map<String,String> headers = new HashMap<>();

        List<List<String>> result = HealthUtils.extractHeaders(headers);

        assertThat(result.size(), is(0));
    }
}
