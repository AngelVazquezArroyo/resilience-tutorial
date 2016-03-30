package de.codecentric.recommendationService;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.codecentric.recommendationService.clients.downstream.ImpostorClientDownStreamFactory;
import de.codecentric.recommendationService.clients.service.ServiceClientAwesomeRecommendationFactory;
import de.codecentric.recommendationService.clients.upstream.ImpostorClientUpStreamFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
/**
 * Created by afitz on 24.03.16.
 */
public class TestConfiguration {

    @Valid
    @NotNull
    private ImpostorClientUpStreamFactory upStreamImpostorFactory = new ImpostorClientUpStreamFactory();

    @Valid
    @NotNull
    private ImpostorClientDownStreamFactory downStreamImpostorFactory = new ImpostorClientDownStreamFactory();

    @Valid
    @NotNull
    private ServiceClientAwesomeRecommendationFactory awesomeRecommendationFactory = new ServiceClientAwesomeRecommendationFactory();

    @JsonProperty("upStreamImpostor")
    public ImpostorClientUpStreamFactory getUpStreamFactory(){
        return upStreamImpostorFactory;
    }

    @JsonProperty("upStreamImpostor")
    public void setUpStreamImppostorFactory(ImpostorClientUpStreamFactory upStreamImpostorFactory) {
        this.upStreamImpostorFactory = upStreamImpostorFactory;
    }

    @JsonProperty("downStreamImpostor")
    public ImpostorClientDownStreamFactory getDownStreamFactory(){
        return downStreamImpostorFactory;
    }

    @JsonProperty("downStreamImpostor")
    public void setDownStreamImpostorFactory(ImpostorClientDownStreamFactory downStreamImpostorFactory) {
        this.downStreamImpostorFactory = downStreamImpostorFactory;
    }

    @JsonProperty("awesomeRecommendationService")
    public ServiceClientAwesomeRecommendationFactory getAwesomeRecommendationFactory() {
        return awesomeRecommendationFactory;
    }

    @JsonProperty("awesomeRecommendationService")
    public void setAwesomeRecommendationFactory(ServiceClientAwesomeRecommendationFactory awesomeRecommendationFactory) {
        this.awesomeRecommendationFactory = awesomeRecommendationFactory;
    }
}
