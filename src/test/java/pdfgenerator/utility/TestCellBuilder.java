package pdfgenerator.utility;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.junit.Test;

import static org.easymock.EasyMock.mock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestCellBuilder {

    @Test
    public void testIBlockElementBuilder() {
        IBlockElement element = new Paragraph();
        Cell cell = CellBuilder.build(element);
        assertThat(cell.getChildren().get(0), is(element));
    }

    @Test
    public void testImageBuilder() {
        Image image = mock(Image.class);
        Cell cell = CellBuilder.build(image);
        assertThat(cell.getChildren().get(0), is(image));
    }
}
