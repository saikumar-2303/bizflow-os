package com.bizflow.app.domain;

import static com.bizflow.app.domain.CustomerOrderTestSamples.*;
import static com.bizflow.app.domain.CustomerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bizflow.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerOrder.class);
        CustomerOrder customerOrder1 = getCustomerOrderSample1();
        CustomerOrder customerOrder2 = new CustomerOrder();
        assertThat(customerOrder1).isNotEqualTo(customerOrder2);

        customerOrder2.setId(customerOrder1.getId());
        assertThat(customerOrder1).isEqualTo(customerOrder2);

        customerOrder2 = getCustomerOrderSample2();
        assertThat(customerOrder1).isNotEqualTo(customerOrder2);
    }

    @Test
    void customerTest() {
        CustomerOrder customerOrder = getCustomerOrderRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        customerOrder.setCustomer(customerBack);
        assertThat(customerOrder.getCustomer()).isEqualTo(customerBack);

        customerOrder.customer(null);
        assertThat(customerOrder.getCustomer()).isNull();
    }
}
