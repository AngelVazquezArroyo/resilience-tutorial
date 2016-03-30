package de.codecentric.recommendationService.health;

import com.codahale.metrics.Meter;
import com.codahale.metrics.health.HealthCheck;

/**
 * Created by afitz on 21.03.16.
 */
public class RequestHealthCheck extends HealthCheck {

    private Meter meter;
    private long maxRequest = 10;

    public RequestHealthCheck(Meter meter) {
        this.meter = meter;
    }

    @Override
    protected Result check() throws Exception {

        long count = meter.getCount();

        if (count <= maxRequest){
            return Result.healthy("count = " + count + " and lower then " + maxRequest);
        }else {
            return Result.unhealthy("count = " + count + " and higher then " + maxRequest);
        }
    }
}
