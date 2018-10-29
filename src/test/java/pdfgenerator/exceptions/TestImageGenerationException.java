package pdfgenerator.exceptions;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNot.not;

public class TestImageGenerationException {

    @Test
    public void TestInitialize() {
        ImageGenerationException ex = new ImageGenerationException("abc");

        assertThat(ex, is(not(nullValue())));
        assertThat(ex.getMessage(), is("abc"));
    }

    @Test
    public void TestInitializeWithException() {
        Exception innerException  = new Exception("Inner");
        ImageGenerationException ex = new ImageGenerationException("abc",innerException);

        assertThat(ex, is(not(nullValue())));
        assertThat(ex.getMessage(), is("abc"));
        assertThat(ex.getCause(), is(not(nullValue())));
        assertThat(ex.getCause().getMessage(), is("Inner"));
    }
}
