package com.github.torkjel.syshealth.worker;

import org.junit.Test;

import com.github.torkjel.syshealth.worker.model.Batch;
import com.github.torkjel.syshealth.worker.model.BatchResponse;
import com.github.torkjel.syshealth.worker.model.Profile;
import com.github.torkjel.syshealth.worker.model.ProfileResponse;
import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;

import static org.assertj.core.api.Assertions.*;

public class DispatcherTest {

    @Test
    public void testDispatcher() {

        Batch b = Batch.builder()
                .batchId("batchId")
                .profile(Profile.builder()
                        .key("p1")
                        .target(Target.builder()
                                .url("u1")
                                .build())
                        .build())
                .build();
/*
        Dispatcher d = new Dispatcher((t) ->
                new CompletedFuture<TargetResult>(
                        TargetResult.builder()
                            .url(t.getUrl())
                            .build(),
                        null),
                b);

        BatchResponse br = d.process();

        assertThat(br).isEqualTo(BatchResponse.builder()
                .batchId("batchId")
                .profile(ProfileResponse.builder()
                        .key("p1")
                        .result(TargetResult.builder()
                                .url("u1")
                                .build())
                        .build())
                .build());
                */
    }
}
