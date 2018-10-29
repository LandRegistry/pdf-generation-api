package pdfgenerationapi.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNot.not;

public class TestJsonTransformer {

    @Test
    public void TestObjectToJson() {
        Map<String, String> obj = new HashMap<>(2);
        obj.put("test1", "test1Value");
        obj.put("test2", "test2Value");
        String result =  JsonTransformer.render(obj);

        assertThat(result, is(not(nullValue())));
        assertThat(result, is("{\"test1\":\"test1Value\",\"test2\":\"test2Value\"}"));
    }

    @Test
    public void TestJsonToObject() {
        JsonTransformer transformer = new JsonTransformer();

        HashMap<String, String> result = (HashMap<String, String>)transformer.parse("{\"test1\":\"test1Value\",\"test2\":\"test2Value\"}", HashMap.class);

        assertThat(result, is(not(nullValue())));
        assertThat(result.get("test1"), is("test1Value"));
        assertThat(result.get("test2"), is("test2Value"));
    }
}
