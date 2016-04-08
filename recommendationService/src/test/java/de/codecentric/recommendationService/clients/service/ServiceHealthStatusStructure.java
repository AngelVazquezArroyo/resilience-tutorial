package de.codecentric.recommendationService.clients.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ServiceHealthStatusStructure {
    // {"AnalyseService":{"healthy":false,"message":"Can't ping AnalyseService: Service
    // unavailable"},"deadlocks":{"healthy":true}}

    private Map<String,Object> statusStructure;

    public ServiceHealthStatusStructure(Map<String, Object> statusStructure) {
        this.statusStructure = statusStructure;
    }

    @JsonProperty
    public Map<String, Object> getStatusStructure() {
        return statusStructure;
    }

    @JsonProperty
    public void setStatusStructure(Map<String, Object> statusStructure) {
        this.statusStructure = statusStructure;
    }
}
