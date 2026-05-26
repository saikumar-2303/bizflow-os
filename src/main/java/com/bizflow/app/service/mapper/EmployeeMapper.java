package com.bizflow.app.service.mapper;

import com.bizflow.app.domain.Employee;
import com.bizflow.app.service.dto.EmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {}
