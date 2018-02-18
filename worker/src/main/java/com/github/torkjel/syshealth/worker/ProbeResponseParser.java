package com.github.torkjel.syshealth.worker;

import org.asynchttpclient.Response;

import com.github.torkjel.syshealth.worker.model.ProbeTargetResult;
import com.github.torkjel.syshealth.worker.model.Status;
import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;

public class ProbeResponseParser implements ResponseParser {

    @Override
    public TargetResult parse(Target target, Response response, long startTime) {
        return ProbeTargetResult.builder()
                .url(target.getUrl())
                .status(Status.builder()
                        .name("probe")
                        .httpStatusCode(response.getStatusCode())
                        .httpStatusReason(response.getStatusText())
                        .code(200 <= response.getStatusCode() && response.getStatusCode() < 300
                            ? Status.Code.SUCCESS
                            : Status.Code.FAILED)
                        .time(System.nanoTime() - startTime)
                        .build())
                .build();
    }

    @Override
    public TargetResult timeout(Target target, long startTime) {
        return ProbeTargetResult.builder()
                .url(target.getUrl())
                .status(Status.builder()
                        .name("probe")
                        .httpStatusCode(0)
                        .httpStatusReason("timed out")
                        .code(Status.Code.TIMEDOUT)
                        .time(System.nanoTime() - startTime)
                        .build())
                .build();
    }

}
