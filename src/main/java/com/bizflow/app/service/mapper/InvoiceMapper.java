package com.bizflow.app.service.mapper;

import com.bizflow.app.domain.Customer;
import com.bizflow.app.domain.Invoice;
import com.bizflow.app.service.dto.CustomerDTO;
import com.bizflow.app.service.dto.InvoiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerName")
    InvoiceDTO toDto(Invoice s);

    @Named("customerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CustomerDTO toDtoCustomerName(Customer customer);
}
