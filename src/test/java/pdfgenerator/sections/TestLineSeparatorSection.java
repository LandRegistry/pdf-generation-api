package pdfgenerator.sections;

import com.itextpdf.layout.element.LineSeparator;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestLineSeparatorSection {

    @Test
    public void testGenerate() throws IOException {
        LineSeparator lineSeparator = LineSeparatorSection.generate();
        assertThat(lineSeparator, is(not(nullValue())));
    }
}
