package de.codecentric.recommendationService.clients;

public class ImpostorResult {
    private int status;
    private String body;

    public ImpostorResult(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
