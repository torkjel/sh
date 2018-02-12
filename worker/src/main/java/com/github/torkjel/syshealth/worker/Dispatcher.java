package com.github.torkjel.syshealth.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.github.torkjel.syshealth.worker.model.Batch;
import com.github.torkjel.syshealth.worker.model.BatchResponse;
import com.github.torkjel.syshealth.worker.model.Profile;
import com.github.torkjel.syshealth.worker.model.ProfileResponse;
import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;

import lombok.SneakyThrows;

public class Dispatcher {

    private final Batch batch;
    private final TargetProcessor targetProcessor;

    private final Map<Profile, List<Future<TargetResult>>> status = new HashMap<>();

    public Dispatcher(TargetProcessor targetProcessor, Batch batch) {
        this.targetProcessor = targetProcessor;
        this.batch = batch;
    }

    @SneakyThrows
    public BatchResponse process() {
        for (Profile p : batch.getProfiles()) {
            List<Future<TargetResult>> profileRestults = new ArrayList<>();
            for (Target t : p.getTargets())
                profileRestults.add(targetProcessor.process(t));
            status.put(p, profileRestults);
        }

        BatchResponse.BatchResponseBuilder builder = BatchResponse
                .builder()
                .batchId(batch.getBatchId());
        for (Map.Entry<Profile, List<Future<TargetResult>>> e : status.entrySet()) {
            ProfileResponse.ProfileResponseBuilder prBuilder = ProfileResponse
                    .builder()
                    .key(e.getKey().getKey());
            List<Future<TargetResult>> futures = e.getValue();
            for (Future<TargetResult> f : futures)
                prBuilder.result(f.get());
            builder.profile(prBuilder.build());
        }

        return builder.build();
    }
}
