package de.codecentric.recommendationService.downstream;

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
public class ImpostorClientDownStreamFactory {

    private static final Logger logger = LoggerFactory.getLogger(ImpostorClientDownStreamFactory.class);

    //    @NotEmpty
    private String host = "localhost";

    //    @NotEmpty
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

    public ImpostorClient build(ImpostorClientDownStreamConfig config) throws ServiceClientException{

        ImpostorClient donwStreamClient = this.build();

        donwStreamClient.setConfig(config);

        return donwStreamClient;
    }

    public ImpostorClient build() throws ServiceClientException{

        CloseableHttpClient httpClient = HttpClients.createDefault();

        ImpostorClient downStream = new ImpostorClientDownStream(this.getHost(), this.getPort(), httpClient);

        logger.info("ImpostorClient build: DownStreamStream");

        downStream.setConfig(ImpostorClientDownStreamConfig.NORMAL);

        return downStream;
    }
}
