package com.github.torkjel.syshealth.mocktarget;

import static spark.Spark.*;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.qmetric.spark.metrics.HealthCheckRoute;

public class Main {
    public static void main(String[] args) {
        new Main().go();
    }

    private void go() {
        port(7080);
        get("/hello", (req, res) -> "Hello world");

        HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        for (int n = 0; n < 10; n++) {
            final int c = n;
            String name = "check-" + c;
            healthCheckRegistry.register(name, new HealthCheck(){
                @Override
                protected Result check() throws Exception {
                    return Math.random() > 0.2
                            ? Result.healthy(name)
                            : Result.unhealthy(name);
                }
            });
        }

        get("/admin", new HealthCheckRoute(healthCheckRegistry));
    }
}
