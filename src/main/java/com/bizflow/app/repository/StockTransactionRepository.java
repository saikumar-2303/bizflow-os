package com.bizflow.app.repository;

import com.bizflow.app.domain.StockTransaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StockTransaction entity.
 */
@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    default Optional<StockTransaction> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<StockTransaction> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<StockTransaction> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select stockTransaction from StockTransaction stockTransaction left join fetch stockTransaction.product",
        countQuery = "select count(stockTransaction) from StockTransaction stockTransaction"
    )
    Page<StockTransaction> findAllWithToOneRelationships(Pageable pageable);

    @Query("select stockTransaction from StockTransaction stockTransaction left join fetch stockTransaction.product")
    List<StockTransaction> findAllWithToOneRelationships();

    @Query(
        "select stockTransaction from StockTransaction stockTransaction left join fetch stockTransaction.product where stockTransaction.id =:id"
    )
    Optional<StockTransaction> findOneWithToOneRelationships(@Param("id") Long id);
}
