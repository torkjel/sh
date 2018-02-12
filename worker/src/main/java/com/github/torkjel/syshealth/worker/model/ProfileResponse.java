package com.github.torkjel.syshealth.worker.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

@Builder
@Getter
@EqualsAndHashCode
public class ProfileResponse {
    private final String key;
    @Singular
    private List<TargetResult> results = new ArrayList<>();
}
