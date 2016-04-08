package de.codecentric.recommendationService.clients.service;

public enum ServiceHealthStatusCode {
    ANALYSIS_SERVICE("AnalysisService"),
    DEADLOCKS("Deadlocks");

    private final String check;

    ServiceHealthStatusCode(String check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return check;
    }
}
