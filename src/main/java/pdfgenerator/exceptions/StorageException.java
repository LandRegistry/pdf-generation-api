package pdfgenerator.exceptions;

/**
 * A custom exception indicating a failed call to the storage-api.
 */
public class StorageException extends Exception {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
