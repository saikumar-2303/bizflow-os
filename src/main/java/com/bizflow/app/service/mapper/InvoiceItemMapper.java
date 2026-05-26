package com.bizflow.app.service.mapper;

import com.bizflow.app.domain.Invoice;
import com.bizflow.app.domain.InvoiceItem;
import com.bizflow.app.domain.Product;
import com.bizflow.app.service.dto.InvoiceDTO;
import com.bizflow.app.service.dto.InvoiceItemDTO;
import com.bizflow.app.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoiceItem} and its DTO {@link InvoiceItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceItemMapper extends EntityMapper<InvoiceItemDTO, InvoiceItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "invoiceInvoiceNumber")
    InvoiceItemDTO toDto(InvoiceItem s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);

    @Named("invoiceInvoiceNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "invoiceNumber", source = "invoiceNumber")
    InvoiceDTO toDtoInvoiceInvoiceNumber(Invoice invoice);
}
