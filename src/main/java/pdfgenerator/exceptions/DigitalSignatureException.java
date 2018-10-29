package pdfgenerator.exceptions;

/**
 * Custom exception for indicating an issue signing the pdf.
 */
public class DigitalSignatureException extends Exception {
    public DigitalSignatureException(String message) {
        super(message);
    }

    public DigitalSignatureException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
