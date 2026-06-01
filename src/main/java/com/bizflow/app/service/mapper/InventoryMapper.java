package com.bizflow.app.service.mapper;

import com.bizflow.app.domain.Inventory;
import com.bizflow.app.domain.Product;
import com.bizflow.app.service.dto.InventoryDTO;
import com.bizflow.app.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inventory} and its DTO {@link InventoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoryMapper extends EntityMapper<InventoryDTO, Inventory> {
    @Mapping(target = "product_id", source = "product_id", qualifiedByName = "productSku")
    InventoryDTO toDto(Inventory s);

    @Named("productSku")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sku", source = "sku")
    ProductDTO toDtoProductSku(Product product);
}
