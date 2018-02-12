package com.github.torkjel.syshealth.worker;

import java.util.concurrent.Future;

import org.asynchttpclient.AsyncHttpClient;

import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;

public class ProbeProcessor implements TargetProcessor {

    private final AsyncHttpClient ahc;
    private final ProbeResponseParser parser;

    public ProbeProcessor(ProbeResponseParser parser, AsyncHttpClient ahc) {
        this.ahc = ahc;
        this.parser = parser;
    }

    @Override
    public Future<TargetResult> process(Target target) {
        return new FutureTargetResult(
                parser,
                target,
                ahc.prepareGet(target.getUrl())
                    .setRequestTimeout(target.getTimeout())
                    .setReadTimeout(target.getTimeout())
                    .execute());
    }
}

