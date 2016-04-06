package de.codecentric.recommendationService.clients.AnalysisClient;

/**
 * Thrown to indicate that a product analysis service client has encountered a problem.
 *
 * @author afitz
 */
public class AnalysisServiceException extends RuntimeException {
    private static final long serialVersionUID = 0;

    public AnalysisServiceException() {
        super();
    }

    public AnalysisServiceException(String message) {
        super(message);
    }

    public AnalysisServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnalysisServiceException(Throwable cause) {
        super(cause);
    }
}
