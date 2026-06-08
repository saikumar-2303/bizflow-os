package com.bizflow.app.repository;

import com.bizflow.app.domain.Stock;
import java.util.Optional;
// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {
    Optional<Stock> findByProductId(Long productId);
    // Optional<Stock> findBySKU(String productSKU);

    // Optional<Stock> findByProductId(String productSKU);

}
