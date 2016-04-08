package de.codecentric.recommendationService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestNormal.class, TestCompleteParameterChecking.class})
public class RecommendationServiceTestSuite {
    @BeforeClass
    public static void setUp() {
        // Nothing to do
    }

    @AfterClass
    public static void tearDown()
    {
        // Nothing to do
    }
}
