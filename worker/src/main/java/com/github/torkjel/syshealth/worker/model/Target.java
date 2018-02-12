package com.github.torkjel.syshealth.worker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Target {
    private final String url;
    private final TargetType type;
    private final int timeout;

    @JsonCreator
    public Target(
            @JsonProperty("url") String url,
            @JsonProperty("type") TargetType type,
            @JsonProperty("timeout") int timeout) {
        this.url = url;
        this.type = type;
        this.timeout = timeout;
    }
}
