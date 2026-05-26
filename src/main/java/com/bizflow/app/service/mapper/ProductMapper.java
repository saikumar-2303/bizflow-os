package com.bizflow.app.service.mapper;

import com.bizflow.app.domain.Product;
import com.bizflow.app.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {}
