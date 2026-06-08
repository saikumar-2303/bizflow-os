package com.bizflow.app.web.rest;

// import org.springdoc.core.annotations.ParameterObject;

import com.bizflow.app.service.StockQueryService;
// import com.bizflow.app.domain.Stock;
// import com.bizflow.app.repository.StockRepository;
import com.bizflow.app.service.StockService;
import com.bizflow.app.service.criteria.StockCriteria;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
public class StockResource {

    private static final Logger LOG = LoggerFactory.getLogger(StockTransactionResource.class);

    private final StockService service;
    // private final StockRepository repository;
    private final StockQueryService queryService;

    public StockResource(StockService service, StockQueryService queryService) {
        this.service = service;
        this.queryService = queryService;
        // this.repository=repository;
    }

    @GetMapping("/stock-details/all")
    public ResponseEntity<List<Map<String, Object>>> getStockDetails(@ParameterObject Pageable pageable, StockCriteria criteria) {
        LOG.debug("REST request to get stock details");
        return ResponseEntity.ok(queryService.getStockDetails(criteria, pageable));
    }

    @GetMapping("/stock-quantity/{id}")
    public ResponseEntity<Map<String, Object>> getStockQuantity(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStockQuantity(id));
    }
}
