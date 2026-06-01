package com.bizflow.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Inventory getInventorySample1() {
        return new Inventory().id(1L).inventory_id("inventory_id1").remarks("remarks1").location("location1").description("description1");
    }

    public static Inventory getInventorySample2() {
        return new Inventory().id(2L).inventory_id("inventory_id2").remarks("remarks2").location("location2").description("description2");
    }

    public static Inventory getInventoryRandomSampleGenerator() {
        return new Inventory()
            .id(longCount.incrementAndGet())
            .inventory_id(UUID.randomUUID().toString())
            .remarks(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
