package com.github.torkjel.syshealth.worker;

import java.util.Map;
import java.util.concurrent.Future;

import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;
import com.github.torkjel.syshealth.worker.model.TargetType;

import lombok.Builder;
import lombok.Singular;

@Builder
public class TargetProcessingService implements TargetProcessor {

    @Singular("processor")
    private Map<TargetType, TargetProcessor> processorsByType;

    public Future<TargetResult> process(Target target) {
        return processorsByType.get(target.getType()).process(target);
    }
}
