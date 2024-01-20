package iegcode.thread;

import java.util.concurrent.atomic.AtomicLong;

public class CounterAtomic {
    private AtomicLong value = new AtomicLong(10L);

    public void Increment(){
        value.incrementAndGet();
    }

    public Long getValue(){
        return value.get();
    }
}
