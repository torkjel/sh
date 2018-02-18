package com.github.torkjel.syshealth.worker;

import java.util.concurrent.Future;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.RequestBuilder;

import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;

public class RequestProcessor implements TargetProcessor {

    private final AsyncHttpClient ahc;
    private final ResponseParser parser;

    public RequestProcessor(ResponseParser parser, AsyncHttpClient ahc) {
        this.ahc = ahc;
        this.parser = parser;
    }

    @Override
    public Future<TargetResult> process(Target target) {
        BoundRequestBuilder brb = ahc.prepareRequest(new RequestBuilder())
                .setMethod(target.getVerb().name())
                .setUrl(target.getUrl())
                .setRequestTimeout(target.getTimeout())
                .setReadTimeout(target.getTimeout())
                .setFollowRedirect(target.isFollowRedirects());
        target.getHeaders().stream().forEach(h -> brb.setHeader(h[0],  h[1]));

        return new FutureTargetResult(parser, target, brb.execute());
    }
}

