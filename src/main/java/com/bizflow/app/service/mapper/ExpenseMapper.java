package com.bizflow.app.service.mapper;

import com.bizflow.app.domain.Employee;
import com.bizflow.app.domain.Expense;
import com.bizflow.app.service.dto.EmployeeDTO;
import com.bizflow.app.service.dto.ExpenseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Expense} and its DTO {@link ExpenseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExpenseMapper extends EntityMapper<ExpenseDTO, Expense> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeName")
    ExpenseDTO toDto(Expense s);

    @Named("employeeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    EmployeeDTO toDtoEmployeeName(Employee employee);
}
