package de.codecentric.recommendationService;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.codecentric.recommendationService.downstream.ImpostorClientDownStreamFactory;
import de.codecentric.recommendationService.upstream.ImpostorClientUpStreamFactory;

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

//    @Valid
//    @NotNull
//    private ServiceClientRecommendationFactory awesomeRecommendationFactory = new ServiceClientRecommendationFactory();

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

//    @JsonProperty("awesomeRecommendationService")
//    public ServiceClientRecommendationFactory getAwesomeRecommendationFactory() {
//        return awesomeRecommendationFactory;
//    }
//
//    @JsonProperty("awesomeRecommendationService")
//    public void setAwesomeRecommendationFactory(ServiceClientRecommendationFactory awesomeRecommendationFactory) {
//        this.awesomeRecommendationFactory = awesomeRecommendationFactory;
//    }
}
