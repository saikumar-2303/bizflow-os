package com.bizflow.app.service.mapper;

import com.bizflow.app.domain.Customer;
import com.bizflow.app.domain.CustomerOrder;
import com.bizflow.app.service.dto.CustomerDTO;
import com.bizflow.app.service.dto.CustomerOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerOrder} and its DTO {@link CustomerOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerOrderMapper extends EntityMapper<CustomerOrderDTO, CustomerOrder> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerName")
    CustomerOrderDTO toDto(CustomerOrder s);

    @Named("customerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CustomerDTO toDtoCustomerName(Customer customer);
}
