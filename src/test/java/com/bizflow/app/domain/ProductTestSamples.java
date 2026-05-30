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
            .sku("sku1")
            .barcode("barcode1")
            .name("name1")
            .category("category1")
            .shape("shape1")
            .retailPack(1)
            .wholesalePack(1)
            .description("description1")
            .stockQuantity(1)
            .lowStockAlert(1)
            .remarks("remarks1")
            .location("location1")
            .message("message1")
            .value("value1");
    }

    public static Product getProductSample2() {
        return new Product()
            .id(2L)
            .sku("sku2")
            .barcode("barcode2")
            .name("name2")
            .category("category2")
            .shape("shape2")
            .retailPack(2)
            .wholesalePack(2)
            .description("description2")
            .stockQuantity(2)
            .lowStockAlert(2)
            .remarks("remarks2")
            .location("location2")
            .message("message2")
            .value("value2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .sku(UUID.randomUUID().toString())
            .barcode(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .shape(UUID.randomUUID().toString())
            .retailPack(intCount.incrementAndGet())
            .wholesalePack(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .stockQuantity(intCount.incrementAndGet())
            .lowStockAlert(intCount.incrementAndGet())
            .remarks(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .message(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString());
    }
}
