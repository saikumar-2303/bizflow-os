package com.bizflow.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExpenseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Expense getExpenseSample1() {
        return new Expense().id(1L).title("title1").category("category1").remarks("remarks1");
    }

    public static Expense getExpenseSample2() {
        return new Expense().id(2L).title("title2").category("category2").remarks("remarks2");
    }

    public static Expense getExpenseRandomSampleGenerator() {
        return new Expense()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .remarks(UUID.randomUUID().toString());
    }
}
