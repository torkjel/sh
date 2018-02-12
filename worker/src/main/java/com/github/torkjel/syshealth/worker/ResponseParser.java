package com.github.torkjel.syshealth.worker;

import org.asynchttpclient.Response;

import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;

interface ResponseParser {
    TargetResult parse(Target target, Response response, long startTime);
}
