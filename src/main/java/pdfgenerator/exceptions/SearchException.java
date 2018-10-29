package pdfgenerator.exceptions;

/**
 * A custom exception indicating a failed call to the search-api.
 */
public class SearchException extends Exception {
    public SearchException(String message) {
        super(message);
    }

    public SearchException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
