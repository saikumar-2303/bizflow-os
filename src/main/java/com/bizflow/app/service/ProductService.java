package com.bizflow.app.service;

import com.bizflow.app.domain.Product;
import com.bizflow.app.repository.ProductRepository;
import com.bizflow.app.service.dto.ProductDTO;
import com.bizflow.app.service.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bizflow.app.domain.Product}.
 */
@Service
@Transactional
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductDTO save(ProductDTO productDTO) {
        LOG.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    /**
     * Update a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductDTO update(ProductDTO productDTO) {
        LOG.debug("Request to update Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    /**
     * Partially update a product.
     *
     * @param productDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductDTO> partialUpdate(ProductDTO productDTO) {
        LOG.debug("Request to partially update Product : {}", productDTO);

        return productRepository
            .findById(productDTO.getId())
            .map(existingProduct -> {
                productMapper.partialUpdate(existingProduct, productDTO);

                return existingProduct;
            })
            .map(productRepository::save)
            .map(productMapper::toDto);
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        LOG.debug("Request to get Product : {}", id);
        return productRepository.findById(id).map(productMapper::toDto);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }

    public List<Product> findAll() {
        LOG.debug("Request to get all Products");
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        LOG.debug("Request to get Product : {}", id);
        return productRepository.findById(id).orElse(null);
    }

    public List<Map<String, Object>> getProductIdAndName() {
        return findAll().stream().map(prod -> Map.<String, Object>of("id", prod.getId(), "name", prod.getSku())).toList();
    }

    public List<Map<String, Object>> getProductIdAndNameWithValueitem(Long id) {
        List<Product> products = findAll();
        List<Map<String, Object>> resultList = new ArrayList<>();

        Product product = productRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("No product found with barcode: " + id));

        if (product == null) {
            throw new EntityNotFoundException("No product found with barcode: " + id);
        }

        String productType = product.getValue();

        String rawProductType = "";

        if (productType.equalsIgnoreCase("Final Product")) {
            rawProductType = "Semi Product";
        } else if (productType.equalsIgnoreCase("Semi Product")) {
            rawProductType = "Raw Product";
        } else if (productType.equalsIgnoreCase("Raw Product")) {
            rawProductType = "Base Product";
        } else {
            throw new IllegalArgumentException("Invalid product type: " + productType);
        }

        for (Product prod : products) {
            if (prod.getValue() != null && prod.getValue().equalsIgnoreCase(rawProductType)) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", prod.getId());
                item.put("name", prod.getSku());
                resultList.add(item);
            }
        }
        return resultList;
    }
}
