package com.alibaba.csp.sentinel.dashboard.util;

import java.util.concurrent.atomic.AtomicInteger;

public class TimeIdGenerator implements IdGenerator<Integer> {

    private AtomicInteger seed = new AtomicInteger(0);

    @Override
    public Integer nextId() {
        int timeseconds = (int) (System.currentTimeMillis()/1000); //2038
        return timeseconds+seed.incrementAndGet();
    }
}
