package com.github.torkjel.syshealth.worker.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class Status {
    private final Code success;
    private final String name;
    private final Long time;
    private final String reason;
    private final List<Status> substatus = new ArrayList<>();

    public static enum Code {
        SUCCESS,
        TIMEDOUT,
        FAILED,
        FLOODED // reponse truncated,
    }
}

