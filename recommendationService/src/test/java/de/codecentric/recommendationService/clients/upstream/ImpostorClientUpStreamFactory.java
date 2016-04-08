package de.codecentric.recommendationService.clients.upstream;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.codecentric.recommendationService.clients.ClientException;
import de.codecentric.recommendationService.clients.ImpostorClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class ImpostorClientUpstreamFactory {
    private String host = "localhost";

    @Min(1)
    @Max(65535)
    private int port = 8080;

    private String path = "/";

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
    public String getPath() {
        return path;
    }

    @JsonProperty
    public void setPath(String path) {
        this.path = path;
    }

    public ImpostorClient build() throws ClientException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ImpostorClient client = new ImpostorClientUpstream(host, port, httpClient);
        client.setConfig(ImpostorClientUpstreamConfig.NORMAL);
        return client;
    }
}
