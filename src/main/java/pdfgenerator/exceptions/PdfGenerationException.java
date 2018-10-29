package pdfgenerator.exceptions;

/**
 * A custom exception indicating a failure to generate an LLC1 PDF.
 */
public class PdfGenerationException extends Exception {
    public PdfGenerationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
