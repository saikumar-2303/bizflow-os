package com.bizflow.app.service.mapper;

import com.bizflow.app.domain.CustomerOrder;
import com.bizflow.app.domain.OrderItem;
import com.bizflow.app.domain.Product;
import com.bizflow.app.service.dto.CustomerOrderDTO;
import com.bizflow.app.service.dto.OrderItemDTO;
import com.bizflow.app.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    @Mapping(target = "order", source = "order", qualifiedByName = "customerOrderOrderNumber")
    OrderItemDTO toDto(OrderItem s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);

    @Named("customerOrderOrderNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "orderNumber", source = "orderNumber")
    CustomerOrderDTO toDtoCustomerOrderOrderNumber(CustomerOrder customerOrder);
}
