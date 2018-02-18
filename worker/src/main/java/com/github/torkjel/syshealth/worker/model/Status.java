package com.github.torkjel.syshealth.worker.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class Status {
    private final Code code;
    private final String name;
    private final Long time;
    private final int httpStatusCode;
    private final String httpStatusReason;

    public static enum Code {
        SUCCESS,
        TIMEDOUT,
        FAILED,
        FLOODED // reponse truncated,
    }

    public boolean success() {
        return code == Code.SUCCESS;
    }
}
