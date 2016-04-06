package de.codecentric.recommendationService.clients.upstream;


import com.fasterxml.jackson.annotation.JsonProperty;
import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ServiceClientException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by afitz on 24.03.16.
 */
public class ImpostorClientUpStreamFactory {

    private static final Logger logger = LoggerFactory.getLogger(ImpostorClientUpStreamFactory.class);

//    @NotEmpty
    private String host = "localhost";

//    @NotEmpty
    @Min(1)
    @Max(65535)
    private int port = 8080;

    private String path = "/";

    @JsonProperty
    public String getHost() {return host;}

    @JsonProperty
    public void setHost(String host) {this.host = host;}

    @JsonProperty
    public int getPort() {return port;}

    @JsonProperty
    public void setPort(int port) {this.port = port;}

    @JsonProperty
    public String getPath() {return path;}

    @JsonProperty
    public void setPath(String path) {
        this.path = path;
    }

    public ImpostorClient build() throws ServiceClientException {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        ImpostorClient upStream = new ImpostorClientUpStream(this.getHost(), this.getPort(), httpClient);

        logger.debug("ImpostorClient get: UpStream");

        upStream.setConfig(ImpostorClientUpStreamConfig.NORMAL);

        return upStream;
    }
}
