package com.bizflow.app.repository;

import com.bizflow.app.domain.Product;
import com.bizflow.app.domain.Stock;
import com.bizflow.app.service.dto.ProductDTO;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findBySku(String productSKU);

    Optional<Product> findByBarcode(String barcode);
}
