package com.bizflow.app.service;

import com.bizflow.app.domain.Product_;
import com.bizflow.app.domain.Stock;
import com.bizflow.app.domain.Stock_;
import com.bizflow.app.repository.StockRepository;
import com.bizflow.app.service.criteria.StockCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class StockQueryService extends QueryService<Stock> {

    private final StockRepository stockRepository;

    public StockQueryService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Map<String, Object>> getStockDetails(StockCriteria criteria, Pageable pageable) {
        Specification<Stock> specification = createSpecification(criteria);

        Page<Stock> stockPage = stockRepository.findAll(specification, pageable);

        return stockPage
            .map(stock -> {
                Map<String, Object> map = new HashMap<>();

                map.put("id", stock.getId());

                if (stock.getProduct() != null) {
                    map.put("productId", stock.getProduct().getId());
                    map.put("productName", stock.getProduct().getName());
                }

                map.put("currentStock", stock.getCurrentStock());
                map.put("reservedStock", stock.getReservedStock());
                map.put("availableStock", stock.getAvailableStock());
                map.put("minimumStock", stock.getMinimumStock());
                map.put("reorderLevel", stock.getReorderLevel());
                map.put("location", stock.getLocation());
                map.put("remarks", stock.getRemarks());
                map.put("lastUpdated", stock.getLastUpdated());

                return map;
            })
            .toList();
    }

    protected Specification<Stock> createSpecification(StockCriteria criteria) {
        Specification<Stock> specification = Specification.where(null);

        if (criteria == null) {
            return specification;
        }

        if (criteria.getDistinct() != null) {
            specification = specification.and(distinct(criteria.getDistinct()));
        }

        if (criteria.getId() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getId(), Stock_.id));
        }

        if (criteria.getCurrentStock() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getCurrentStock(), Stock_.currentStock));
        }

        if (criteria.getReservedStock() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getReservedStock(), Stock_.reservedStock));
        }

        if (criteria.getAvailableStock() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getAvailableStock(), Stock_.availableStock));
        }

        if (criteria.getMinimumStock() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getMinimumStock(), Stock_.minimumStock));
        }

        if (criteria.getReorderLevel() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getReorderLevel(), Stock_.reorderLevel));
        }

        if (criteria.getLocation() != null) {
            specification = specification.and(buildStringSpecification(criteria.getLocation(), Stock_.location));
        }

        if (criteria.getRemarks() != null) {
            specification = specification.and(buildStringSpecification(criteria.getRemarks(), Stock_.remarks));
        }

        if (criteria.getLastUpdated() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getLastUpdated(), Stock_.lastUpdated));
        }

        if (criteria.getProductId() != null) {
            specification = specification.and(
                buildSpecification(criteria.getProductId(), root -> root.join(Stock_.product, JoinType.LEFT).get(Product_.id))
            );
        }

        if (criteria.getProductName() != null && criteria.getProductName().getContains() != null) {
            specification = specification.and((root, query, cb) ->
                cb.like(
                    cb.lower(root.join(Stock_.product, JoinType.LEFT).get(Product_.name)),
                    "%" + criteria.getProductName().getContains().toLowerCase() + "%"
                )
            );
        }

        return specification;
    }
}
