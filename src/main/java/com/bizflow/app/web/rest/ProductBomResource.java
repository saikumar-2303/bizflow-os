package com.bizflow.app.web.rest;

import com.bizflow.app.repository.ProductBomRepository;
import com.bizflow.app.service.HelperService;
import com.bizflow.app.service.ProductBomService;
import com.bizflow.app.service.dto.Response;
import java.util.Map;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-bom")
public class ProductBomResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerOrderResource.class);

    private final ProductBomService productBomService;

    private final ProductBomRepository productBomRepository;

    private final HelperService helperService;

    public ProductBomResource(ProductBomService productBomService, ProductBomRepository productBomRepository, HelperService helperService) {
        this.productBomService = productBomService;
        this.productBomRepository = productBomRepository;
        this.helperService = helperService;
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createProductBom(@RequestBody Map<String, Object> inputMap) {
        LOG.info("Received request to create product bom with input: {}", inputMap);
        Response response = productBomService.createProductBom(inputMap);
        return ResponseEntity.ok(response);
    }
}
