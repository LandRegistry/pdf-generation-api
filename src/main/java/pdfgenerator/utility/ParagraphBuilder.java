package pdfgenerator.utility;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.element.Paragraph;
import pdfgenerator.resources.Fonts;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A utility class for building pre-configured IText 7 {@link Paragraph} elements.
 */
public final class ParagraphBuilder {
    private static final String MISSING_INFORMATION = "Not provided";
    private static final float DEFAULT_LINE_SPACING = 1;

    private ParagraphBuilder() {}

    /**
     * Builds and returns a {@link Paragraph}, configured with the transBoldFont.
     * @param content The content to populate the paragraph with.
     * @return The constructed {@link Paragraph}.
     * @throws IOException If the font file could not be read from disk.
     */
    public static Paragraph heading(String content) throws IOException {
        PdfFont transBoldFont = Fonts.getInstance().getTransBoldFont();
        return build(content)
            .setFont(transBoldFont);
    }

    /**
     * Builds and returns a {@link Paragraph}, configured with the geoHeadingFont.
     * @param content The content to populate the paragraph with.
     * @return The constructed {@link Paragraph}.
     * @throws IOException If the font file could not be read from disk.
     */
    public static Paragraph geoheading(String content) throws IOException {
        PdfFont geoHeadingFont = Fonts.getInstance().getGeoHeadingFont();
        return build(content)
            .setFont(geoHeadingFont);
    }

    /**
     * Builds and returns a {@link Paragraph}, configured with transFont, and sub heading font size.
     * @param content The content to populate the paragraph with.
     * @return The constructed {@link Paragraph}.
     * @throws IOException If the font file could not be read from disk.
     */
    public static Paragraph subheading(String content) throws IOException {
        PdfFont transFont = Fonts.getInstance().getTransFont();
        return build(content)
            .setFont(transFont)
            .setFontSize(22);
    }

    /**
     * Builds and returns a {@link Paragraph}, configured with transFont and font size appropriate for regular text.
     * @param content The content to populate the paragraph with.
     * @return The constructed {@link Paragraph}.
     * @throws IOException If the font file could not be read from disk.
     */
    public static Paragraph normal(String content) throws IOException {
        PdfFont transFont = Fonts.getInstance().getTransFont();
        return build(content)
            .setFont(transFont)
            .setFontSize(11);
    }

    /**
     * Builds and returns a {@link Paragraph}, configured with transBoldFont and font size appropriate for regular text.
     * @param content The content to populate the paragraph with.
     * @return The constructed {@link Paragraph}.
     * @throws IOException If the font file could not be read from disk.
     */
    public static Paragraph bold(String content) throws IOException {
        PdfFont transBoldFont = Fonts.getInstance().getTransBoldFont();
        return build(content)
            .setFont(transBoldFont)
            .setFontSize(11);
    }

    /**
     * Builds and returns a {@link Paragraph}, configured with transFont and font size appropriate for regular text.
     * @param date The content to populate the paragraph with.
     * @return The constructed {@link Paragraph}.
     * @throws IOException If the font file could not be read from disk.
     */
    public static Paragraph normal(Date date) throws IOException {
        String content = date == null ? MISSING_INFORMATION : new SimpleDateFormat("d MMMM yyyy").format(date);
        return normal(content);
    }

    /**
     * Builds and returns a {@link Paragraph}.
     * @param input The content to populate the paragraph with. If the input is null the paragraph will be set with
     *              MISSING_INFORMATION String.
     * @return The constructed {@link Paragraph}.
     */
    private static Paragraph build(String input) {
        String content = input;
        if (content == null) {
            content = MISSING_INFORMATION;
        }

        return new Paragraph()
            .setMultipliedLeading(DEFAULT_LINE_SPACING)
            .add(content);
    }

    /**
     * Builds and returns a {@link Paragraph}, configured with transBoldFont and font size appropriate for a watermark.
     * @param content The content to populate the paragraph with.
     * @return The constructed {@link Paragraph}.
     * @throws IOException If the font file could not be read from disk.
     */
    public static Paragraph watermark(String content) throws IOException {
        PdfFont transBoldFont = Fonts.getInstance().getTransBoldFont();
        return new Paragraph()
        	.add(content)
            .setFont(transBoldFont)
            .setFontSize(175);
    }
}
