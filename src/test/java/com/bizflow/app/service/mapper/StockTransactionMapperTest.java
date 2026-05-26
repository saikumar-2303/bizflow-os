package com.bizflow.app.service.mapper;

import static com.bizflow.app.domain.StockTransactionAsserts.*;
import static com.bizflow.app.domain.StockTransactionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockTransactionMapperTest {

    private StockTransactionMapper stockTransactionMapper;

    @BeforeEach
    void setUp() {
        stockTransactionMapper = new StockTransactionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStockTransactionSample1();
        var actual = stockTransactionMapper.toEntity(stockTransactionMapper.toDto(expected));
        assertStockTransactionAllPropertiesEquals(expected, actual);
    }
}
