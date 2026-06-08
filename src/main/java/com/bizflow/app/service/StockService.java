package com.bizflow.app.service;

import com.bizflow.app.domain.Stock;
import com.bizflow.app.repository.ProductRepository;
import com.bizflow.app.repository.StockRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockService {

    private final StockRepository repository;

    private final ProductRepository productRepository;

    public StockService(StockRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    // private Optional<List<Stock>> getStock() {
    //     return Optional.ofNullable(repository.findAll());
    // }

    public Optional<Stock> getStockById(Long id) {
        return repository.findById(id);
    }

    // public Optional<Stock> getbyProductSKU(String productSKU) {
    //     return productRepository.findBySku(productSKU);
    // }

    public Stock save(Stock stock) {
        return repository.save(stock);
    }

    // public List<Map<String, Object>> getStockDetails(@ParameterObject Pageable pageable, StockCriteria criteria) {
    //     pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
    //             Sort.by(Sort.Direction.DESC, "lastUpdated"));
    //     if (criteria.getProductId() != null) {
    //         specification = specification.and(
    //                 buildSpecification(
    //                         criteria.getProductId(),
    //                         root -> root.join(Stock_.product, JoinType.LEFT).get(Product_.id)));
    //     }

    //     if (criteria.getProductName() != null) {
    //         specification = specification.and(
    //                 buildStringSpecification(
    //                         criteria.getProductName(),
    //                         root -> root.join(Stock_.product, JoinType.LEFT).get(Product_.name)));
    //     }
    //     List<Map<String, Object>> stockDetailsList = new ArrayList<>();
    //     for (Stock s : getStock().get()) {
    //         Map<String, Object> stockDetails = new HashMap<>();
    //         stockDetails.put("available_stock", s.getAvailableStock());
    //         stockDetailsList.add(stockDetails);
    //     }
    //     return stockDetailsList;
    // }

    public Map<String, Object> getStockQuantity(Long id) {
        Map<String, Object> stockQuantity = new HashMap<>();
        Optional<Stock> stockOpt = getStockById(id);
        if (stockOpt.isPresent()) {
            stockQuantity.put("stock_quantity", stockOpt.get().getAvailableStock());
        }
        return stockQuantity;
    }

    public Optional<Stock> getbyProductId(Long productId) {
        return repository.findByProductId(productId);
    }
}
