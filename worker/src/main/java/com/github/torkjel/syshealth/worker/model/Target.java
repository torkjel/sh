package com.github.torkjel.syshealth.worker.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Target {
    private final TargetType type;
    private final HttpVerb verb;
    private final String url;
    private final int timeout;
    private final boolean followRedirects;
    private final List<String[]> headers;

    @JsonCreator
    public Target(
            @JsonProperty(value = "type", required = true) TargetType type,
            @JsonProperty(value = "verb", required = true) HttpVerb verb,
            @JsonProperty(value = "url", required = true) String url,
            @JsonProperty(value = "timeout", required = false) int timeout,
            @JsonProperty(value = "follow-redirects", required = false) boolean followRedirects,
            @JsonProperty(value = "headers", required = false) List<String[]> headers) {
            // Payload. Need object. encoding...
        this.type = req(type);
        this.verb = req(verb);
        this.url = req(url);
        this.timeout = or(timeout, 2000);
        this.followRedirects = or(followRedirects, true);
        this.headers = or(headers, new ArrayList<>());
    }

    private <T> T or(T value, T fallback) {
        return value != null ? value : fallback;
    }

    private <T> T req(T v) {
        if (v == null)
            throw new NullPointerException();
        return v;
    }

}
