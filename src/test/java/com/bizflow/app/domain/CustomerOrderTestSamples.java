package com.bizflow.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CustomerOrder getCustomerOrderSample1() {
        return new CustomerOrder().id(1L).orderNumber("orderNumber1").status("status1").remarks("remarks1");
    }

    public static CustomerOrder getCustomerOrderSample2() {
        return new CustomerOrder().id(2L).orderNumber("orderNumber2").status("status2").remarks("remarks2");
    }

    public static CustomerOrder getCustomerOrderRandomSampleGenerator() {
        return new CustomerOrder()
            .id(longCount.incrementAndGet())
            .orderNumber(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .remarks(UUID.randomUUID().toString());
    }
}
