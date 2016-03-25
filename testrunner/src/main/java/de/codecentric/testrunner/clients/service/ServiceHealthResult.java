package de.codecentric.testrunner.clients.service;

/**
 * Created by afitz on 24.03.16.
 */
public class ServiceHealthResult {
    private int statusCode;
    private String message;
    private String entity;

    public ServiceHealthResult(int statusCode, String message, String entity) {
        this.statusCode = statusCode;
        this.message = message;
        this.entity = entity;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getEntity() {
        return entity;
    }
}
