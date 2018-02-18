package com.github.torkjel.syshealth.worker;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Supplier;

public class Singleton<T> {

    private T value;

    public synchronized T get(Supplier<T> s) {
        return value != null
            ? value
            : (value = s.get());
    }

}
