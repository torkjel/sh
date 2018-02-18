package com.github.torkjel.syshealth.worker.model;

import java.util.Map;

import com.github.torkjel.syshealth.worker.model.hc.HealthCheck;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class HealthCheckTargetResult extends TargetResult {

    private final Map<String, HealthCheck> healthChecks;

    @Builder(builderClassName = "builder")
    private HealthCheckTargetResult(
            String url,
            Status status,
            @Singular Map<String, HealthCheck> healthChecks) {
        super(url, status);
        this.healthChecks = healthChecks;
    }

    public HealthCheck getHealthCheck(String name) {
        return healthChecks.get(name);
    }
}
