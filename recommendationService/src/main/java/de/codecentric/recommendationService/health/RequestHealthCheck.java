package de.codecentric.recommendationService.health;

import com.codahale.metrics.Meter;
import com.codahale.metrics.health.HealthCheck;

/**
 * Health check for number of requests received.
 *
 * @author afitz
 */
public class RequestHealthCheck extends HealthCheck {

    private Meter meter;
    private long maxRequests = 10;

    public RequestHealthCheck(Meter meter) {
        this.meter = meter;
    }

    @Override
    protected Result check() throws Exception {
        long count = meter.getCount();

        if (count <= maxRequests) {
            return Result.healthy("#request is " + count + ", lower than maximum of " + maxRequests);
        } else {
            return Result.unhealthy("#request is " + count + ", higher than maximum of " +
                    maxRequests);
        }
    }
}
