package com.bizflow.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Invoice getInvoiceSample1() {
        return new Invoice().id(1L).invoiceNumber("invoiceNumber1").paymentStatus("paymentStatus1").remarks("remarks1");
    }

    public static Invoice getInvoiceSample2() {
        return new Invoice().id(2L).invoiceNumber("invoiceNumber2").paymentStatus("paymentStatus2").remarks("remarks2");
    }

    public static Invoice getInvoiceRandomSampleGenerator() {
        return new Invoice()
            .id(longCount.incrementAndGet())
            .invoiceNumber(UUID.randomUUID().toString())
            .paymentStatus(UUID.randomUUID().toString())
            .remarks(UUID.randomUUID().toString());
    }
}
