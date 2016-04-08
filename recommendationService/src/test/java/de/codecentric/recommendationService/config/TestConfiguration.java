package de.codecentric.recommendationService.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.codecentric.recommendationService.clients.downstream.ImpostorClientDownstreamFactory;
import de.codecentric.recommendationService.clients.upstream.ImpostorClientUpstreamFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TestConfiguration {
    @Valid
    @NotNull
    private ImpostorClientUpstreamFactory upstreamImpostorFactory =
            new ImpostorClientUpstreamFactory();

    @Valid
    @NotNull
    private ImpostorClientDownstreamFactory downstreamImpostorFactory =
            new ImpostorClientDownstreamFactory();

    @JsonProperty("upStreamImpostor")
    public ImpostorClientUpstreamFactory getUpstreamFactory() {
        return upstreamImpostorFactory;
    }

    @JsonProperty("upStreamImpostor")
    public void setUpstreamImpostorFactory(ImpostorClientUpstreamFactory upstreamImpostorFactory) {
        this.upstreamImpostorFactory = upstreamImpostorFactory;
    }

    @JsonProperty("downStreamImpostor")
    public ImpostorClientDownstreamFactory getDownstreamFactory() {
        return downstreamImpostorFactory;
    }

    @JsonProperty("downStreamImpostor")
    public void setDownstreamImpostorFactory(ImpostorClientDownstreamFactory downstreamImpostorFactory) {
        this.downstreamImpostorFactory = downstreamImpostorFactory;
    }
}
