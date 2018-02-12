package com.github.torkjel.syshealth.worker;

import java.util.concurrent.Future;

import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;

interface TargetProcessor {
    Future<TargetResult> process(Target taget);
}
