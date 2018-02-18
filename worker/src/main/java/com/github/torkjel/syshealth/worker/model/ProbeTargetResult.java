package com.github.torkjel.syshealth.worker.model;

import lombok.Builder;
import lombok.ToString;

@ToString(callSuper = true)
public class ProbeTargetResult extends TargetResult {

    @Builder
    private ProbeTargetResult(String url, Status status) {
        super(url, status);
    }
}
