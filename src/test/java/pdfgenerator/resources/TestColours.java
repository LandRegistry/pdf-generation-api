package pdfgenerator.resources;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestColours {

    @Test
    public void testColorStream() throws IOException {
        InputStream colour = Colours.getInstance().getColour();
        assertThat(colour, is(not(nullValue())));
    }
}
