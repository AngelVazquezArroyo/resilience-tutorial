package de.codecentric.testrunner.clients.service;

/**
 * Created by afitz on 24.03.16.
 */
public enum ServiceHealthStatusCode {

    ANALYSESERVICE("AnalyseService"),
    DEADLOCKS("deadlocks")
    ;

    private final String check;

    ServiceHealthStatusCode(String check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return check;
    }
}
