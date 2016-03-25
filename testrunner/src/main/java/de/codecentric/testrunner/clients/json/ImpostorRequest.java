package de.codecentric.testrunner.clients.json;

import java.util.List;

/**
 * Created by afitz on 23.03.16.
 */
public class ImpostorRequest {

    private int number;
    private String verb;
    private String resource;
    private List<Param> params;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }
}
