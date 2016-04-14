package de.codecentric.recommendationService.clients.AnalysisClient;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import org.apache.http.client.HttpClient;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class AnalysisServiceFactory {

    @NotEmpty
    private String host;

    @Min(1)
    @Max(65535)
    private int port = 5672;


    @Min(0)
    @Max(65535)
    private int portFailover = 0;

    @NotEmpty
    private String path;

    @Min(1)
    @Max(60000)
    private long timeout;

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
    public int getPortFailover() {
        return portFailover;
    }

    @JsonProperty
    public void setPortFailover(int portFailover) {
        this.portFailover = portFailover;
    }

    @JsonProperty
    public String getPath() {
        return path;
    }

    @JsonProperty
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty
    public long getTimeout() {
        return timeout;
    }

    @JsonProperty
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public AnalysisServiceClient build(Environment environment) {
        final HttpClientConfiguration httpConfig = new HttpClientConfiguration();
        httpConfig.setTimeout(Duration.milliseconds(getTimeout()));
        final HttpClient httpClient = new HttpClientBuilder(environment).using(httpConfig)
                .build("analysis-http-client");

        AnalysisServiceClient client = new AnalysisServiceClientAdapter(getHost(), getPort(),
                getPortFailover(), getPath(), httpClient);

        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() {
            }

            @Override
            public void stop() {
            }
        });

        return client;
    }
}
