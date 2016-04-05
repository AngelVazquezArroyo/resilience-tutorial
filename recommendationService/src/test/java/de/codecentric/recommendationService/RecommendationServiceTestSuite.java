package de.codecentric.recommendationService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by afitz on 31.03.16.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({TestBulkheads.class, TestNormal.class})
public class RecommendationServiceTestSuite {

    @BeforeClass
    public static void setUp() {

        System.out.println("setting up RecommendationServiceTestSuite");
    }

    @AfterClass
    public static void tearDown()
    {
        System.out.println("tearing downStream RecommendationServiceTestSuite");
    }

}
