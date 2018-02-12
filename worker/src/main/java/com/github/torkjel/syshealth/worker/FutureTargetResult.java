package com.github.torkjel.syshealth.worker;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.asynchttpclient.Response;

import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;

public class FutureTargetResult implements Future<TargetResult> {

    private final Future<Response> delegate;
    private final ResponseParser parser;
    private final Target target;
    private final long startTime;

    public FutureTargetResult(ResponseParser parser, Target target, Future<Response> delegate) {
       this.delegate = delegate;
       this.target = target;
       this.parser = parser;
       this.startTime = System.nanoTime();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return delegate.cancel(mayInterruptIfRunning);
    }

    @Override
    public TargetResult get() throws InterruptedException, ExecutionException {
        return parse(delegate.get());
    }

    @Override
    public TargetResult get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return parse(delegate.get(timeout, unit));
    }

    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public boolean isDone() {
        return delegate.isDone();
    }

    private TargetResult parse(Response response) {
        return parser.parse(target, response, startTime);
    }
}
