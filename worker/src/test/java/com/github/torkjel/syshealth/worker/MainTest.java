package com.github.torkjel.syshealth.worker;

import java.util.concurrent.Future;

import org.junit.Test;

import com.github.torkjel.syshealth.worker.Main;
import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetResult;

public class MainTest {

    private final int port = 9999;

    @Test
    public void testRequest() {
        Main main = new Main(port, new TargetProcessor() {

            @Override
            public Future<TargetResult> process(Target taget) {
                return null;
            }
        });
    }
}
