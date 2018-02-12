package com.github.torkjel.syshealth.worker.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
@Getter
public class Profile {
    private final String key;
    @Singular
    private List<Target> targets;

    @JsonCreator
    public Profile(
            @JsonProperty("key") String key,
            @JsonProperty("targets") List<Target> targets) {
        this.key = key;
        this.targets = targets;
    }
}
