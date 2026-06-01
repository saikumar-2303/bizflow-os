package com.bizflow.app.domain;

import static com.bizflow.app.domain.InventoryTestSamples.*;
import static com.bizflow.app.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bizflow.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventory.class);
        Inventory inventory1 = getInventorySample1();
        Inventory inventory2 = new Inventory();
        assertThat(inventory1).isNotEqualTo(inventory2);

        inventory2.setId(inventory1.getId());
        assertThat(inventory1).isEqualTo(inventory2);

        inventory2 = getInventorySample2();
        assertThat(inventory1).isNotEqualTo(inventory2);
    }

    @Test
    void product_idTest() {
        Inventory inventory = getInventoryRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        inventory.setProduct_id(productBack);
        assertThat(inventory.getProduct_id()).isEqualTo(productBack);

        inventory.product_id(null);
        assertThat(inventory.getProduct_id()).isNull();
    }
}
