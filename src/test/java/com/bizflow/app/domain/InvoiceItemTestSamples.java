package com.bizflow.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InvoiceItem getInvoiceItemSample1() {
        return new InvoiceItem().id(1L).quantity(1);
    }

    public static InvoiceItem getInvoiceItemSample2() {
        return new InvoiceItem().id(2L).quantity(2);
    }

    public static InvoiceItem getInvoiceItemRandomSampleGenerator() {
        return new InvoiceItem().id(longCount.incrementAndGet()).quantity(intCount.incrementAndGet());
    }
}
