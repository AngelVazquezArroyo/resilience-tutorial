package de.codecentric.testrunner.clients.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.codecentric.testrunner.clients.ServiceClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by afitz on 24.03.16.
 */
public class ServiceClientAwesomeRecommendationFactory {
    private String host;

    @Min(1)
    @Max(65535)
    private int port;

    @Min(1)
    @Max(65535)
    private int portAdmin;
    private String path;

    @JsonProperty
    public String getHost() {
        return host;
    }

    @JsonProperty
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty
    public int getPort() {
        return port;
    }

    @JsonProperty
    public void setPort(int port) {
        this.port = port;
    }

    @JsonProperty
    public int getPortAdmin() {
        return portAdmin;
    }

    @JsonProperty
    public void setPortAdmin(int portAdmin) {
        this.portAdmin = portAdmin;
    }

    @JsonProperty
    public String getPath() {
        return path;
    }

    @JsonProperty
    public void setPath(String path) {
        this.path = path;
    }

    public ServiceClient build(){

        CloseableHttpClient httpClient = HttpClients.createDefault();

//        System.out.println("host: " + host);

        System.out.println("Service Client build - Awesome Recommendation");
        ServiceClient awesomeRecommendation = new ServiceClientAwesomeRecommendation(host, port, portAdmin, path, httpClient);

        System.out.println(awesomeRecommendation == null ? "error" : "success");

        return awesomeRecommendation;
    }
}
