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

public class TestChargeCountSection {

    @Test
    public void testFormattingAndHeading() throws IOException {
        int expectedCount = 10;
        float expectedPaddingTop = 10f;
        float expectedPaddingBottom = 15f;

        Table table = ChargeCountSection.generate(expectedCount);

        Cell expectedCell = table.getCell(0,0);
        Cell expectedHeaderCell = table.getHeader().getCell(0, 0);

        String actualHeaderText = ((Text)((Paragraph)expectedHeaderCell.getChildren().get(0)).getChildren().get(0)).getText();

        assertThat(table, is(not(nullValue())));
        assertThat(table.getColumnWidth(0).getValue(), is(100.0f));
        assertThat(actualHeaderText, is("Number of charges section"));
        assertThat(expectedCell.getPaddingTop().getValue(), is(expectedPaddingTop));
        assertThat(expectedCell.getPaddingBottom().getValue(), is(expectedPaddingBottom));
    }

    @Test
    public void testGeneratePluralText() throws IOException {
        int expectedCount = 10;
        String expectedText = "There are 10 local land charges in this area";

        Table table = ChargeCountSection.generate(expectedCount);

        Cell expectedCell = table.getCell(0,0);

        String actualText = ((Text)((Paragraph)expectedCell.getChildren().get(0)).getChildren().get(0)).getText();

        assertThat(expectedText, is(actualText));
    }

    @Test
    public void testGenerateSinglarText() throws IOException {
        int expectedCount = 1;
        String expectedText = "There is 1 local land charge in this area";

        Table table = ChargeCountSection.generate(expectedCount);

        Cell expectedCell = table.getCell(0,0);

        String actualText = ((Text)((Paragraph)expectedCell.getChildren().get(0)).getChildren().get(0)).getText();

        assertThat(expectedText, is(actualText));
    }
}
