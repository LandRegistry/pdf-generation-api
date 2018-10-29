package pdfgenerator.exceptions;

/**
 * A custom exception indicating a failure to generate an image.
 */
public class ImageGenerationException extends Exception {
    public ImageGenerationException(String message) {
        super(message);
    }

    public ImageGenerationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
