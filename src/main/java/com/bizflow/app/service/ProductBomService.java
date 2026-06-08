package com.bizflow.app.service;

import com.bizflow.app.domain.Product;
import com.bizflow.app.domain.ProductBom;
import com.bizflow.app.repository.ProductBomRepository;
import com.bizflow.app.repository.ProductRepository;
import com.bizflow.app.service.dto.Response;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductBomService {

    private static final Logger LOG = LoggerFactory.getLogger(StockTransactionService.class);

    private final HelperService helperService;

    private final ProductRepository productRepository;

    private final ProductBomRepository productBomRepository;

    public ProductBomService(HelperService helperService, ProductRepository productRepository, ProductBomRepository productBomRepository) {
        this.helperService = helperService;
        this.productRepository = productRepository;
        this.productBomRepository = productBomRepository;
    }

    public Response createProductBom(Map<String, Object> inputMap) {
        LOG.info("Creating product bom with input: {}", inputMap);

        // 1. Extract Parent Finished Product (Works perfectly!)
        @SuppressWarnings("unchecked")
        Map<String, Object> finishedProductMap = (Map<String, Object>) inputMap.get("finishedProduct");
        Long finishedProductId = ((Number) finishedProductMap.get("id")).longValue();

        Product finishedProduct = productRepository
            .findById(finishedProductId)
            .orElseThrow(() -> new EntityNotFoundException("Finished product not found with ID: " + finishedProductId));

        // 2. Extract the materials array
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> components = (List<Map<String, Object>>) inputMap.get("items");

        if (components != null) {
            for (Map<String, Object> component : components) {
                // 🎯 FIX 2: Look for "id" instead of "rawMaterialId"
                if (component.get("rawMaterialId") != null) {
                    Long rawMaterialId = ((Number) component.get("rawMaterialId")).longValue();
                    Long requiredQuantity = ((Number) component.get("quantity")).longValue();

                    Product rawMaterial = productRepository
                        .findById(rawMaterialId)
                        .orElseThrow(() -> new EntityNotFoundException("Raw material not found with ID: " + rawMaterialId));

                    // 3. Construct and save your BOM Entity row
                    ProductBom productBom = new ProductBom();
                    productBom.setFinishedProduct(finishedProduct);
                    productBom.setRawMaterial(rawMaterial);
                    productBom.setQuantityRequired(requiredQuantity); // Uses the root level quantity

                    // 🎯 NOTE: Don't forget to call your repository save method here!
                    // productBomRepository.save(productBom);
                    productBomRepository.save(productBom);

                    LOG.info("Successfully mapped Component ID: {}, Quantity: {}", rawMaterialId, requiredQuantity);
                }
            }
        }

        return helperService.getResponse("BOM created Successfully", HttpStatus.CREATED);
    }
}
