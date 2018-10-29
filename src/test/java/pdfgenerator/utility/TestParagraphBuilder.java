package pdfgenerator.utility;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.UnitValue;

import pdfgenerator.resources.Fonts;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestParagraphBuilder {
    private final int FONT_PROPERTY = 20;
    private final int FONT_SIZE_PROPERTY = 24;
    
    @Before
    public void setup() throws IOException {
    	Fonts.getInstance().initFonts();
    }

    @Test
    public void testHeading() throws IOException{

        Paragraph result = ParagraphBuilder.heading("Heading");

        assertThat(result, is(not(nullValue())));
        assertThat(((PdfFont)result.getProperty(FONT_PROPERTY)).getFontProgram().toString(), is("GDSTransportWebsite-Bold"));
        assertThat(((Text)result.getChildren().get(0)).getText(), is("Heading"));
    }

    @Test
    public void testGeoHeading() throws IOException{

        Paragraph result = ParagraphBuilder.geoheading("Heading");

        assertThat(result, is(not(nullValue())));
        assertThat(((PdfFont)result.getProperty(FONT_PROPERTY)).getFontProgram().toString(), is("GeoHeadline"));
        assertThat(((Text)result.getChildren().get(0)).getText(), is("Heading"));
    }

    @Test
    public void testSubheading() throws IOException{
        UnitValue expectedFontSize = UnitValue.createPointValue(22);

        Paragraph result = ParagraphBuilder.subheading("subheading");

        assertThat(result, is(not(nullValue())));
        assertThat(((PdfFont)result.getProperty(FONT_PROPERTY)).getFontProgram().toString(), is("GDSTransportWebsite"));
        assertThat(result.getProperty(FONT_SIZE_PROPERTY), is(expectedFontSize));
        assertThat(((Text)result.getChildren().get(0)).getText(), is("subheading"));
    }

    @Test
    public void testSubheadingNull() throws IOException{
        UnitValue expectedFontSize = UnitValue.createPointValue(22);

        Paragraph result = ParagraphBuilder.subheading(null);

        assertThat(result, is(not(nullValue())));
        assertThat(((PdfFont)result.getProperty(FONT_PROPERTY)).getFontProgram().toString(), is("GDSTransportWebsite"));
        assertThat(result.getProperty(FONT_SIZE_PROPERTY), is(expectedFontSize));
        assertThat(((Text)result.getChildren().get(0)).getText(), is("Not provided"));
    }

    @Test
    public void testNormal() throws IOException{
        UnitValue expectedFontSize = UnitValue.createPointValue(11);

        Paragraph result = ParagraphBuilder.normal("normal");

        assertThat(result, is(not(nullValue())));
        assertThat(((PdfFont)result.getProperty(FONT_PROPERTY)).getFontProgram().toString(), is("GDSTransportWebsite"));
        assertThat(result.getProperty(FONT_SIZE_PROPERTY), is(expectedFontSize));
        assertThat(((Text)result.getChildren().get(0)).getText(), is("normal"));
    }

    @Test
    public void testBold() throws IOException{
        UnitValue expectedFontSize = UnitValue.createPointValue(11);

        Paragraph result = ParagraphBuilder.bold("bold");

        assertThat(result, is(not(nullValue())));
        assertThat(((PdfFont)result.getProperty(FONT_PROPERTY)).getFontProgram().toString(), is("GDSTransportWebsite-Bold"));
        assertThat(result.getProperty(FONT_SIZE_PROPERTY), is(expectedFontSize));
        assertThat(((Text)result.getChildren().get(0)).getText(), is("bold"));
    }

    @Test
    public void testDate() throws IOException{
        UnitValue expectedFontSize = UnitValue.createPointValue(11);

        Date date = new Date();
        String dateString = new SimpleDateFormat("d MMMM yyyy").format(date);
        Paragraph result = ParagraphBuilder.normal(date);

        assertThat(result, is(not(nullValue())));
        assertThat(((PdfFont)result.getProperty(FONT_PROPERTY)).getFontProgram().toString(), is("GDSTransportWebsite"));
        assertThat(result.getProperty(FONT_SIZE_PROPERTY), is(expectedFontSize));
        assertThat(((Text)result.getChildren().get(0)).getText(), is(dateString));
    }

    @Test
    public void testDateNull() throws IOException{
        UnitValue expectedFontSize = UnitValue.createPointValue(11);

        Date date = null;
        Paragraph result = ParagraphBuilder.normal(date);

        assertThat(result, is(not(nullValue())));
        assertThat(((PdfFont)result.getProperty(FONT_PROPERTY)).getFontProgram().toString(), is("GDSTransportWebsite"));
        assertThat(result.getProperty(FONT_SIZE_PROPERTY), is(expectedFontSize));
        assertThat(((Text)result.getChildren().get(0)).getText(), is("Not provided"));
    }
}