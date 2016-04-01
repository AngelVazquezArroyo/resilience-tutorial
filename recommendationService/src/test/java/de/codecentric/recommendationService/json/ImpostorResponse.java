package de.codecentric.recommendationService.json;

import java.util.List;

/**
 * Created by afitz on 23.03.16.
 */
public class ImpostorResponse {

    private String verb;
    private String url;
    private List<Param> headers;
    private String body;


    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Param> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Param> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
//    {
//        "verb" : "GET",
//            "url" : "/recommendation-db?product=P00T",
//            "headers" : [
//        { "name" : "Content-Type", "value" : "application/json"}
//        ],
//        "body" : "{ \"products\" : [\"P001\"] }"
//    },

}
