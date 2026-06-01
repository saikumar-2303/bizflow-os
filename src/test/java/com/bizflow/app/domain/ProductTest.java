package com.bizflow.app.domain;

import static com.bizflow.app.domain.InventoryTestSamples.*;
import static com.bizflow.app.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bizflow.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void inventory_idTest() {
        Product product = getProductRandomSampleGenerator();
        Inventory inventoryBack = getInventoryRandomSampleGenerator();

        product.addInventory_id(inventoryBack);
        assertThat(product.getInventory_ids()).containsOnly(inventoryBack);
        assertThat(inventoryBack.getProduct_id()).isEqualTo(product);

        product.removeInventory_id(inventoryBack);
        assertThat(product.getInventory_ids()).doesNotContain(inventoryBack);
        assertThat(inventoryBack.getProduct_id()).isNull();

        product.inventory_ids(new HashSet<>(Set.of(inventoryBack)));
        assertThat(product.getInventory_ids()).containsOnly(inventoryBack);
        assertThat(inventoryBack.getProduct_id()).isEqualTo(product);

        product.setInventory_ids(new HashSet<>());
        assertThat(product.getInventory_ids()).doesNotContain(inventoryBack);
        assertThat(inventoryBack.getProduct_id()).isNull();
    }
}
