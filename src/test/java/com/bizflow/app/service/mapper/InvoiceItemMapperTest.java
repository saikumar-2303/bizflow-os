package com.bizflow.app.service.mapper;

import static com.bizflow.app.domain.InvoiceItemAsserts.*;
import static com.bizflow.app.domain.InvoiceItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceItemMapperTest {

    private InvoiceItemMapper invoiceItemMapper;

    @BeforeEach
    void setUp() {
        invoiceItemMapper = new InvoiceItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceItemSample1();
        var actual = invoiceItemMapper.toEntity(invoiceItemMapper.toDto(expected));
        assertInvoiceItemAllPropertiesEquals(expected, actual);
    }
}
