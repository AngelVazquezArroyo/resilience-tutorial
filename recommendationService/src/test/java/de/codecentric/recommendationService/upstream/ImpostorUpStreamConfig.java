package de.codecentric.recommendationService.upstream;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.codecentric.recommendationService.json.ImpostorRequest;

import java.util.List;

/**
 * Created by afitz on 24.03.16.
 */
// ******************
// *** deprecated ***
// ******************

public class ImpostorUpStreamConfig {

    private String mode;
    private String failuremode;
    private String protocol;
    private String server;
    private int    delay;

    @JsonProperty
    public int getDelay() {
        return delay;
    }

    @JsonProperty
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @JsonProperty("requests")
    private List<ImpostorRequest> requests;

    @JsonProperty
    public String getMode() {
        return mode;
    }

    @JsonProperty
    public void setMode(String mode) {
        this.mode = mode;
    }

    @JsonProperty
    public String getFailuremode() {
        return failuremode;
    }

    @JsonProperty
    public void setFailuremode(String failuremode) {
        this.failuremode = failuremode;
    }

    @JsonProperty
    public String getProtocol() {
        return protocol;
    }

    @JsonProperty
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @JsonProperty
    public String getServer() {
        return server;
    }

    @JsonProperty
    public void setServer(String server) {
        this.server = server;
    }

    @JsonProperty("requests")
    public List<ImpostorRequest> getRequests() {
        return requests;
    }

    @JsonProperty("requests")
    public void setRequests(List<ImpostorRequest> requests) {
        this.requests = requests;
    }

}

//{
//        "mode" : "upstream",
//        "failuremode" : "none",
//        "protocol" : "http",
//        "server" : "localhost:8101",
//        "requests" : [
//        {
//        "number" : 1,
//        "verb" : "GET",
//        "resource" : "recommendation",
//        "params" : [
//        { "name" : "user", "value" : "U001"},
//        { "name" : "product", "value" : "P002"}
//        ]
//        },
//        {
//        "number" : 2,
//        "verb" : "GET",
//        "resource" : "recommendation",
//        "params" : [
//        { "name" : "user", "value" : "U005"},
//        { "name" : "product", "value" : "P001"}
//        ]
//        },
//        {
//        "number" : 3,
//        "verb" : "GET",
//        "resource" : "recommendation",
//        "params" : [
//        { "name" : "user", "value" : "U003"},
//        { "name" : "product", "value" : "P00T"}
//        ]
//        }
//        ]
//        }

