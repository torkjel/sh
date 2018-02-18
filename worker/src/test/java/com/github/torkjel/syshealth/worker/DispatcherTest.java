package com.github.torkjel.syshealth.worker;

import org.junit.Ignore;
import org.junit.Test;

import com.github.torkjel.syshealth.worker.model.Batch;
import com.github.torkjel.syshealth.worker.model.BatchResponse;
import com.github.torkjel.syshealth.worker.model.HttpVerb;
import com.github.torkjel.syshealth.worker.model.ProbeTargetResult;
import com.github.torkjel.syshealth.worker.model.Profile;
import com.github.torkjel.syshealth.worker.model.ProfileResponse;
import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;
import com.github.torkjel.syshealth.worker.model.TargetType;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.util.concurrent.Future;

public class DispatcherTest {

    @Ignore
    @Test
    public void testDispatcher() {

        Batch b = Batch.builder()
                .batchId("batchId")
                .profile(Profile.builder()
                        .key("p1")
                        .target(Target.builder()
                                .type(TargetType.probe)
                                .verb(HttpVerb.GET)
                                .url("u1")
                                .build())
                        .build())
                .build();

        Dispatcher d = new Dispatcher(
                (t) -> {
                    @SuppressWarnings("unchecked")
                    Future<TargetResult> f = mock(Future.class);
                    try {
                        when(f.get()).thenReturn(ProbeTargetResult.builder()
                                .url(t.getUrl())
                            .build());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return f;
                },
                b);

        BatchResponse br = d.process();

        assertThat(br).isEqualTo(BatchResponse.builder()
                .batchId("batchId")
                .profile(ProfileResponse.builder()
                        .key("p1")
                        .result(ProbeTargetResult.builder()
                                .url("u1")
                                .build())
                        .build())
                .build());
    }
}
