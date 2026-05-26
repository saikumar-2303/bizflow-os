package com.bizflow.app.domain;

import static com.bizflow.app.domain.CustomerTestSamples.*;
import static com.bizflow.app.domain.InvoiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bizflow.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = getInvoiceSample1();
        Invoice invoice2 = new Invoice();
        assertThat(invoice1).isNotEqualTo(invoice2);

        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);

        invoice2 = getInvoiceSample2();
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    void customerTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        invoice.setCustomer(customerBack);
        assertThat(invoice.getCustomer()).isEqualTo(customerBack);

        invoice.customer(null);
        assertThat(invoice.getCustomer()).isNull();
    }
}
