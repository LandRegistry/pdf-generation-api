package pdfgenerator.exceptions;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestSearchException {

    @Test
    public void TestInitialize() {
        SearchException ex = new SearchException("abc");

        assertThat(ex, is(not(nullValue())));
        assertThat(ex.getMessage(), is("abc"));
    }

    @Test
    public void TestInitializeWithException() {
        Exception innerException  = new Exception("Inner");
        SearchException ex = new SearchException("abc",innerException);

        assertThat(ex, is(not(nullValue())));
        assertThat(ex.getMessage(), is("abc"));
        assertThat(ex.getCause(), is(not(nullValue())));
        assertThat(ex.getCause().getMessage(), is("Inner"));
    }
}
