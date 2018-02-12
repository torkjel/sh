package com.github.torkjel.syshealth.worker.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class TargetResult {
    private final String url;
    private final Status status;
}
