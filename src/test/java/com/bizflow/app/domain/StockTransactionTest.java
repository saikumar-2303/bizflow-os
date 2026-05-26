package com.bizflow.app.domain;

import static com.bizflow.app.domain.ProductTestSamples.*;
import static com.bizflow.app.domain.StockTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bizflow.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockTransaction.class);
        StockTransaction stockTransaction1 = getStockTransactionSample1();
        StockTransaction stockTransaction2 = new StockTransaction();
        assertThat(stockTransaction1).isNotEqualTo(stockTransaction2);

        stockTransaction2.setId(stockTransaction1.getId());
        assertThat(stockTransaction1).isEqualTo(stockTransaction2);

        stockTransaction2 = getStockTransactionSample2();
        assertThat(stockTransaction1).isNotEqualTo(stockTransaction2);
    }

    @Test
    void productTest() {
        StockTransaction stockTransaction = getStockTransactionRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        stockTransaction.setProduct(productBack);
        assertThat(stockTransaction.getProduct()).isEqualTo(productBack);

        stockTransaction.product(null);
        assertThat(stockTransaction.getProduct()).isNull();
    }
}
