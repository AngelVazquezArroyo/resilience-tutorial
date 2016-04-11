package de.codecentric.recommendationService.impostor;

public class ImpostorException extends RuntimeException {
    public ImpostorException() {
        super();
    }

    public ImpostorException(String message) {
        super(message);
    }

    public ImpostorException(Throwable cause) {
        super(cause);
    }

    public ImpostorException(String message, Throwable cause) {
        super(message, cause);
    }
}
