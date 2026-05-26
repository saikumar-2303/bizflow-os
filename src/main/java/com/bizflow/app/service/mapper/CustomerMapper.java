package com.bizflow.app.service.mapper;

import com.bizflow.app.domain.Customer;
import com.bizflow.app.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {}
