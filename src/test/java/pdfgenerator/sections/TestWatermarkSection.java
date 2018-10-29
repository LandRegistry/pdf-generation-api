package pdfgenerator.sections;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestWatermarkSection {

    @Test
    public void testGenerate() throws IOException {
        Paragraph watermark = WatermarkSection.generate("TEST");
        assertThat(watermark, is(not(nullValue())));
        assertThat(((Text)watermark.getChildren().get(0)).getText(), is("TEST"));
    }
}
