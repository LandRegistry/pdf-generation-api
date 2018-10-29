package pdfgenerator.resources;

import com.itextpdf.kernel.font.PdfFont;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestFonts {

    @Test
    public void testGetGeo() throws IOException{
    	Fonts.getInstance().initFonts();
        PdfFont font = Fonts.getInstance().getTransFont();
        assertThat(font, is(not(nullValue())));
        assertThat(font.getFontProgram().toString(), is("GDSTransportWebsite"));
    }

    @Test
    public void testGetGeoBold() throws IOException{
    	Fonts.getInstance().initFonts();
        PdfFont font = Fonts.getInstance().getTransBoldFont();
        assertThat(font, is(not(nullValue())));
        assertThat(font.getFontProgram().toString(), is("GDSTransportWebsite-Bold"));
    }

    @Test
    public void testGetGeoHeading() throws IOException{
    	Fonts.getInstance().initFonts();
        PdfFont font = Fonts.getInstance().getGeoHeadingFont();
        assertThat(font, is(not(nullValue())));
        assertThat(font.getFontProgram().toString(), is("GeoHeadline"));
    }

}
