package com.bizflow.app.service.mapper;

import static com.bizflow.app.domain.ExpenseAsserts.*;
import static com.bizflow.app.domain.ExpenseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpenseMapperTest {

    private ExpenseMapper expenseMapper;

    @BeforeEach
    void setUp() {
        expenseMapper = new ExpenseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExpenseSample1();
        var actual = expenseMapper.toEntity(expenseMapper.toDto(expected));
        assertExpenseAllPropertiesEquals(expected, actual);
    }
}
