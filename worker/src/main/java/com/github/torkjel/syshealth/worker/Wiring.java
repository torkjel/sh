package com.github.torkjel.syshealth.worker;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

import org.asynchttpclient.AsyncHttpClient;

import com.github.torkjel.syshealth.worker.model.TargetType;

public class Wiring {

    private Singleton<AsyncHttpClient> httpClient = new Singleton<>();

    public AsyncHttpClient getAsyncHttpClient() {
        return httpClient.get(() -> asyncHttpClient(config()));
    }

    public TargetProcessingService getTargetProcessingService() {
        return TargetProcessingService.builder()
            .processor(
                TargetType.probe,
                new RequestProcessor(
                    new ProbeResponseParser(),
                    getAsyncHttpClient()))
            .processor(
                TargetType.healthcheck,
                new RequestProcessor(
                    new HealtCheckResponseParser(),
                    getAsyncHttpClient()))
            .build();
    }

    public int getPort() {
        return 9080;

    }

}
