package de.codecentric.recommendationService;

import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ServiceClient;
import de.codecentric.recommendationService.clients.ClientException;
import de.codecentric.recommendationService.clients.service.ServiceClientRecommendationFactory;
import de.codecentric.recommendationService.config.LoadTestConfiguration;
import de.codecentric.recommendationService.config.TestConfiguration;
import de.codecentric.recommendationService.rules.ImpostorRule;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TestName;

import static org.junit.Assert.fail;

/**
 * A test base class that starts the impostors before the tests and shuts them down again after
 * the tests. All actual test classes should be derived from this class.
 *
 * @author afitz
 */
public class RecommendationTestCase {
    private static TestConfiguration config = new LoadTestConfiguration().get();

    private static ImpostorClient impostorUpstreamClient = null;
    private static ImpostorClient impostorDownstreamClient = null;
    private static ServiceClient recommendationServiceClient = null;

    @Rule
    public TestName name = new TestName();

    @ClassRule
    public static final DropwizardAppRule<RecommendationConfiguration> SERVICE_RULE =
            new DropwizardAppRule<>(RecommendationService.class,
                    ResourceHelpers.resourceFilePath("recommendationServiceConfiguration.yml"));

    @ClassRule
    public static final ImpostorRule IMPOSTOR_RULE = new ImpostorRule(config);

    @BeforeClass
    public static void initialize() {
        try {
            impostorUpstreamClient = config.getUpstreamFactory().build();
            impostorDownstreamClient = config.getDownstreamFactory().build();
            recommendationServiceClient = new ServiceClientRecommendationFactory(SERVICE_RULE)
                    .build("recommendationServiceClient client");
        } catch (ClientException e) {
            fail(e.getMessage());
        }
    }

    @AfterClass
    public static void tearDown() {
        // nothing to do
    }

    public static ImpostorClient getImpostorUpstreamClient() {
        return impostorUpstreamClient;
    }

    public static ImpostorClient getImpostorDownstreamClient() {
        return impostorDownstreamClient;
    }

    public static ServiceClient getRecommendationServiceClient() {
        return recommendationServiceClient;
    }
}
