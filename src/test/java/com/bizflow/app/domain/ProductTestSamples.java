package com.bizflow.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product()
            .id(1L)
            .name("name1")
            .category("category1")
            .description("description1")
            .stockQuantity(1)
            .lowStockAlert(1)
            .barcode("barcode1");
    }

    public static Product getProductSample2() {
        return new Product()
            .id(2L)
            .name("name2")
            .category("category2")
            .description("description2")
            .stockQuantity(2)
            .lowStockAlert(2)
            .barcode("barcode2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .stockQuantity(intCount.incrementAndGet())
            .lowStockAlert(intCount.incrementAndGet())
            .barcode(UUID.randomUUID().toString());
    }
}
