package com.github.torkjel.syshealth.worker;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.asynchttpclient.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.torkjel.syshealth.worker.model.HealthCheckTargetResult;
import com.github.torkjel.syshealth.worker.model.Status;
import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;
import com.github.torkjel.syshealth.worker.model.hc.HealthCheck;

public class HealtCheckResponseParser implements ResponseParser {

    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public TargetResult parse(Target target, Response response, long startTime) {
        Map<String, HealthCheck> healthCheckResponse = parse(response);

        HealthCheckTargetResult.builder builder = HealthCheckTargetResult.builder()
                .url(target.getUrl())
                .status(Status.builder()
                        .name("probe")
                        .httpStatusCode(response.getStatusCode())
                        .httpStatusReason(response.getStatusText())
                        .code(200 <= response.getStatusCode() && response.getStatusCode() < 300
                            ? Status.Code.SUCCESS
                            : Status.Code.FAILED)
                        .time(System.nanoTime() - startTime)
                        .build());

        healthCheckResponse.entrySet().stream()
            .forEach((e) -> builder.healthCheck(e.getKey(), e.getValue()));

        return builder.build();
    }


    @Override
    public TargetResult timeout(Target target, long startTime) {
        return HealthCheckTargetResult.builder()
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

    private Map<String, HealthCheck> parse(Response response) {
        if (response.getContentType().contains("json")) {
            try {
                return OM
                        .readerFor(new TypeReference<Map<String, HealthCheck>>() {})
                        .readValue(responseBody(response));
            } catch (Exception e) {
                return handleError(response, Optional.of(e));
            }
        } else {
            return handleError(response, Optional.empty());
        }
    }

    private String responseBody(Response response) {
        return response.getResponseBody(Charset.forName("utf-8"));
    }

    private Map<String, HealthCheck> handleError(Response response, Optional<Exception> e) {
        return Collections.singletonMap(
                "parse error",
                new HealthCheck(
                    false,
                    "Failed to parse health check response. " +
                            "Check json format and content type " +
                            "(" + response.getContentType() + "): \n" +
                            responseBody(response),
                    e.map(Exception::getMessage).orElse("")));
    }

}
