package com.github.torkjel.syshealth.worker.model.hc;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Getter
@Builder
public class HealthChecks {

    @Singular
    private final Map<String, HealthCheck> healthChecks;
}
