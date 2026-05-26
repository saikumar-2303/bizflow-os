package com.bizflow.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StockTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StockTransaction getStockTransactionSample1() {
        return new StockTransaction()
            .id(1L)
            .transactionType("transactionType1")
            .quantity(1)
            .previousStock(1)
            .newStock(1)
            .remarks("remarks1");
    }

    public static StockTransaction getStockTransactionSample2() {
        return new StockTransaction()
            .id(2L)
            .transactionType("transactionType2")
            .quantity(2)
            .previousStock(2)
            .newStock(2)
            .remarks("remarks2");
    }

    public static StockTransaction getStockTransactionRandomSampleGenerator() {
        return new StockTransaction()
            .id(longCount.incrementAndGet())
            .transactionType(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .previousStock(intCount.incrementAndGet())
            .newStock(intCount.incrementAndGet())
            .remarks(UUID.randomUUID().toString());
    }
}
