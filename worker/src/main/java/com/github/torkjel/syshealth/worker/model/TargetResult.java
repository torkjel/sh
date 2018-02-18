package com.github.torkjel.syshealth.worker.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class TargetResult {

    private final String url;
    private final Status status;

    protected TargetResult(String url, Status status) {
        this.url = url;
        this.status = status;
    }

    public boolean success() {
        return status.success();
    }

}
