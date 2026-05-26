package com.bizflow.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bizflow.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockTransactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockTransactionDTO.class);
        StockTransactionDTO stockTransactionDTO1 = new StockTransactionDTO();
        stockTransactionDTO1.setId(1L);
        StockTransactionDTO stockTransactionDTO2 = new StockTransactionDTO();
        assertThat(stockTransactionDTO1).isNotEqualTo(stockTransactionDTO2);
        stockTransactionDTO2.setId(stockTransactionDTO1.getId());
        assertThat(stockTransactionDTO1).isEqualTo(stockTransactionDTO2);
        stockTransactionDTO2.setId(2L);
        assertThat(stockTransactionDTO1).isNotEqualTo(stockTransactionDTO2);
        stockTransactionDTO1.setId(null);
        assertThat(stockTransactionDTO1).isNotEqualTo(stockTransactionDTO2);
    }
}
