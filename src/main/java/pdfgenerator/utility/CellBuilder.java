package pdfgenerator.utility;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;

/**
 * A utility class for building pre-configured IText 7 {@link Cell} elements.
 */
public final class CellBuilder {
    private static final float DEFAULT_PADDING = 0;

    private CellBuilder() {}

    /**
     * Builds and returns a {@link Cell} with the given row and col span, with the given content.
     * @param rowSpan The number of rows this cell should expand across.
     * @param colSpan The number of columns this cell should expand across.
     * @param content The content to populate into the cell.
     * @return The constructed {@link Cell}.
     */
    public static Cell build(Integer rowSpan, Integer colSpan, IBlockElement... content) {
        Cell cell = new Cell(rowSpan, colSpan)
            .setPadding(DEFAULT_PADDING)
            .setBorder(Border.NO_BORDER);

        for (IBlockElement element : content) {
            cell.add(element);
        }
        return cell;
    }

    /**
     * Builds and returns a {@link Cell} with the given row and col span, with the given content.
     * @param rowSpan The number of rows this cell should expand across.
     * @param colSpan The number of columns this cell should expand across.
     * @param content The content to populate into the cell.
     * @return The constructed {@link Cell}.
     */
    public static Cell build(Integer rowSpan, Integer colSpan, Image... content) {
        Cell cell = new Cell(rowSpan, colSpan)
            .setBorder(Border.NO_BORDER);

        for (Image element : content) {
            cell.add(element);
        }
        return cell;
    }

    /**
     * Builds and returns a {@link Cell} with the given content. With a default row and col span of 1.
     * @param content The content to populate into the cell.
     * @return The constructed {@link Cell}.
     */
    public static Cell build(IBlockElement... content) {
        return build(1, 1, content);
    }

    /**
     * Builds and returns a {@link Cell} with the given content. With a default row and col span of 1.
     * @param content The content to populate into the cell.
     * @return The constructed {@link Cell}.
     */
    public static Cell build(Image... content) {
        return build(1, 1, content);
    }
}
