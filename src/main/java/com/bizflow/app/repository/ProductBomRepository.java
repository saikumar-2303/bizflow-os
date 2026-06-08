package com.bizflow.app.repository;

import com.bizflow.app.domain.ProductBom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBomRepository extends JpaRepository<ProductBom, Long>, JpaSpecificationExecutor<ProductBom> {}
