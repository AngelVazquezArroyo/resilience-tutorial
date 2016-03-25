package de.codecentric.testrunner.clients.downstream;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.codecentric.testrunner.clients.ImpostorClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by afitz on 24.03.16.
 */
public class ImpostorClientDownStreamFactory {

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

    public ImpostorClient build (ImpostorClientDownStreamConfig config){

        ImpostorClient donwStreamClient = this.build();

        donwStreamClient.setConfig(config);

        return donwStreamClient;
    }

    public ImpostorClient build(){

        CloseableHttpClient httpClient = HttpClients.createDefault();

        ImpostorClient downStream = new ImpostorClientDownStream(this.getHost(), this.getPort(), httpClient);

        System.out.println("ImpostorClient build: DownStreamStream");

        downStream.setConfig(ImpostorClientDownStreamConfig.NORMAL);

        System.out.println("ImpostorClient config: NORMAL");

        return downStream;
    }
}
