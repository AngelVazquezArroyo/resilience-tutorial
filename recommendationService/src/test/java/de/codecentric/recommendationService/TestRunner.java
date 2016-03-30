package de.codecentric.recommendationService;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by afitz on 23.03.16.
 */
public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(SampleTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
    }
}
