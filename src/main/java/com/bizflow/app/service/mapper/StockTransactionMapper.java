package com.bizflow.app.service.mapper;

import com.bizflow.app.domain.Product;
import com.bizflow.app.domain.StockTransaction;
import com.bizflow.app.service.dto.ProductDTO;
import com.bizflow.app.service.dto.StockTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StockTransaction} and its DTO {@link StockTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockTransactionMapper extends EntityMapper<StockTransactionDTO, StockTransaction> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    StockTransactionDTO toDto(StockTransaction s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
