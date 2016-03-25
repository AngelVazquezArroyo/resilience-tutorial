package de.codecentric.testrunner.clients.downstream;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.codecentric.testrunner.clients.json.ImpostorResponse;

import java.util.List;

/**
 * Created by afitz on 24.03.16.
 */
// ******************
// *** deprecated ***
// ******************
public class ImpostorDownStreamConfig {

    private String mode;
    private String failuremode;
    private String protocol;
    private List<Integer> latency;
    private String latencymode;
    private List<ImpostorResponse> responses;

    @JsonProperty("latencymode")
    public String getLatencymode() {
        return latencymode;
    }

    @JsonProperty("latencymode")
    public void setLatencymode(String latencymode) {
        this.latencymode = latencymode;
    }

    @JsonProperty("latency")
    public List<Integer> getLatency() {
        return latency;
    }

    @JsonProperty("latency")
    public void setLatency(List<Integer> latency) {
        this.latency = latency;
    }

    @JsonProperty("mode")
    public String getMode() {return mode;}

    @JsonProperty("mode")
    public void setMode(String mode) {this.mode = mode;}

    @JsonProperty("failuremode")
    public String getFailuremode() {return failuremode;}

    @JsonProperty("failuremode")
    public void setFailuremode(String failuremode) {this.failuremode = failuremode;}

    @JsonProperty("protocol")
    public String getProtocol() {return protocol;}

    @JsonProperty("protocol")
    public void setProtocol(String protocol) {this.protocol = protocol;}

    @JsonProperty("responses")
    public List<ImpostorResponse> getResponses() {return responses;}

    @JsonProperty("responses")
    public void setResponses(List<ImpostorResponse> responses) {this.responses = responses;}

//    {
//        "mode" : "downstream",
//            "failuremode" : "none",
//            "protocol" : "http",
//            "latency" : [5000, 0],
//            "responses" : [
//        {
//            "verb" : "GET",
//                "url" : "/recommendation-db?product=P00T",
//                "headers" : [
//            { "name" : "Content-Type", "value" : "application/json"}
//            ],
//            "body" : "{ \"products\" : [\"P001\"] }"
//        },
//        {
//            "verb" : "GET",
//                "url" : "/recommendation-db?product=P001",
//                "headers" : [
//            { "name" : "Content-Type", "value" : "application/json"}
//            ],
//            "body" : "{ \"products\" : [\"P002\"] }"
//        },
//        {
//            "verb" : "GET",
//                "url" : "/recommendation-db?product=P002",
//                "headers" : [
//            { "name" : "Content-Type", "value" : "application/json"}
//            ],
//            "body" : "{ \"products\" : [\"P003\"] }"
//        },
//        {
//            "verb" : "GET",
//                "url" : "/recommendation-db?product=P003",
//                "headers" : [
//            { "name" : "Content-Type", "value" : "application/json"}
//            ],
//            "body" : "{ \"products\" : [\"P003\"] }"
//        }
//        ]
//    }

}
