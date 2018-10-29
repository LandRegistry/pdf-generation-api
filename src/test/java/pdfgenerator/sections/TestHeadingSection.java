package pdfgenerator.sections;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.UnitValue;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestHeadingSection {

    @Test
    public void testGenerate() throws IOException {
        Table table = HeadingSection.generate();
       
        int fontProperty = 24;
        UnitValue mainHeadingFontSize = UnitValue.createPointValue(28);
        UnitValue subheadingFontSize = UnitValue.createPointValue(24);

        assertThat(table, is(not(nullValue())));
        assertThat(table.getColumnWidth(0).getValue(), is(90.0f));
        assertThat(table.getColumnWidth(1).getValue(), is(10.0f));
        assertThat(((Text)((Paragraph)((Cell)table.getHeader().getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).getText(),
                is("Organisation"));
        assertThat(((Text)((Paragraph)((Cell)table.getHeader().getChildren().get(1)).getChildren().get(0)).getChildren().get(0)).getText(),
                is("Organisation icon"));
        assertThat(table.getCell(0,0).getChildren().get(0).getProperty(fontProperty), is(mainHeadingFontSize));
        assertThat(((Text)((Paragraph)table.getCell(0,0).getChildren().get(0)).getChildren().get(0)).getText(),
                is("HM Land Registry"));
        assertThat(table.getCell(0,0).getChildren().get(1).getProperty(fontProperty), is(subheadingFontSize));
        assertThat(((Text)((Paragraph)table.getCell(0,0).getChildren().get(1)).getChildren().get(0)).getText(),
                is("Local land charges official search"));
    }
}