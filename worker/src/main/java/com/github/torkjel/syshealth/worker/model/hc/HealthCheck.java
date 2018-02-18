package com.github.torkjel.syshealth.worker.model.hc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HealthCheck {
    private final boolean healthy;
    private final String message;
    private final String error;

    public HealthCheck(boolean status) {
        this(status, null);
    }

    public HealthCheck(boolean status, String message) {
        this(status, message, null);
    }

    @JsonCreator
    public HealthCheck(
            @JsonProperty(value = "healthy", required = true) boolean healthy,
            @JsonProperty(value = "message", required = false) String messeage,
            @JsonProperty(value = "error", required = false) String error) {
        this.healthy = healthy;
        this.message = messeage;
        this.error = error;
    }


}
