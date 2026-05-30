package com.bizflow.app.web.rest;

import static com.bizflow.app.domain.ProductAsserts.*;
import static com.bizflow.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bizflow.app.IntegrationTest;
import com.bizflow.app.domain.Product;
import com.bizflow.app.repository.ProductRepository;
import com.bizflow.app.service.dto.ProductDTO;
import com.bizflow.app.service.mapper.ProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_SHAPE = "AAAAAAAAAA";
    private static final String UPDATED_SHAPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_RETAIL_PACK = 1;
    private static final Integer UPDATED_RETAIL_PACK = 2;
    private static final Integer SMALLER_RETAIL_PACK = 1 - 1;

    private static final Integer DEFAULT_WHOLESALE_PACK = 1;
    private static final Integer UPDATED_WHOLESALE_PACK = 2;
    private static final Integer SMALLER_WHOLESALE_PACK = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_STOCK_QUANTITY = 1;
    private static final Integer UPDATED_STOCK_QUANTITY = 2;
    private static final Integer SMALLER_STOCK_QUANTITY = 1 - 1;

    private static final Integer DEFAULT_LOW_STOCK_ALERT = 1;
    private static final Integer UPDATED_LOW_STOCK_ALERT = 2;
    private static final Integer SMALLER_LOW_STOCK_ALERT = 1 - 1;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    private Product insertedProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity() {
        return new Product()
            .sku(DEFAULT_SKU)
            .barcode(DEFAULT_BARCODE)
            .name(DEFAULT_NAME)
            .category(DEFAULT_CATEGORY)
            .shape(DEFAULT_SHAPE)
            .retailPack(DEFAULT_RETAIL_PACK)
            .wholesalePack(DEFAULT_WHOLESALE_PACK)
            .description(DEFAULT_DESCRIPTION)
            .stockQuantity(DEFAULT_STOCK_QUANTITY)
            .lowStockAlert(DEFAULT_LOW_STOCK_ALERT)
            .remarks(DEFAULT_REMARKS)
            .location(DEFAULT_LOCATION)
            .message(DEFAULT_MESSAGE)
            .value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity() {
        return new Product()
            .sku(UPDATED_SKU)
            .barcode(UPDATED_BARCODE)
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .shape(UPDATED_SHAPE)
            .retailPack(UPDATED_RETAIL_PACK)
            .wholesalePack(UPDATED_WHOLESALE_PACK)
            .description(UPDATED_DESCRIPTION)
            .stockQuantity(UPDATED_STOCK_QUANTITY)
            .lowStockAlert(UPDATED_LOW_STOCK_ALERT)
            .remarks(UPDATED_REMARKS)
            .location(UPDATED_LOCATION)
            .message(UPDATED_MESSAGE)
            .value(UPDATED_VALUE);
    }

    @BeforeEach
    void initTest() {
        product = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProduct != null) {
            productRepository.delete(insertedProduct);
            insertedProduct = null;
        }
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        var returnedProductDTO = om.readValue(
            restProductMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductDTO.class
        );

        // Validate the Product in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProduct = productMapper.toEntity(returnedProductDTO);
        assertProductUpdatableFieldsEquals(returnedProduct, getPersistedProduct(returnedProduct));

        insertedProduct = returnedProduct;
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setSku(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBarcodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setBarcode(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].shape").value(hasItem(DEFAULT_SHAPE)))
            .andExpect(jsonPath("$.[*].retailPack").value(hasItem(DEFAULT_RETAIL_PACK)))
            .andExpect(jsonPath("$.[*].wholesalePack").value(hasItem(DEFAULT_WHOLESALE_PACK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].stockQuantity").value(hasItem(DEFAULT_STOCK_QUANTITY)))
            .andExpect(jsonPath("$.[*].lowStockAlert").value(hasItem(DEFAULT_LOW_STOCK_ALERT)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.shape").value(DEFAULT_SHAPE))
            .andExpect(jsonPath("$.retailPack").value(DEFAULT_RETAIL_PACK))
            .andExpect(jsonPath("$.wholesalePack").value(DEFAULT_WHOLESALE_PACK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.stockQuantity").value(DEFAULT_STOCK_QUANTITY))
            .andExpect(jsonPath("$.lowStockAlert").value(DEFAULT_LOW_STOCK_ALERT))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku equals to
        defaultProductFiltering("sku.equals=" + DEFAULT_SKU, "sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku in
        defaultProductFiltering("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU, "sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku is not null
        defaultProductFiltering("sku.specified=true", "sku.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySkuContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku contains
        defaultProductFiltering("sku.contains=" + DEFAULT_SKU, "sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku does not contain
        defaultProductFiltering("sku.doesNotContain=" + UPDATED_SKU, "sku.doesNotContain=" + DEFAULT_SKU);
    }

    @Test
    @Transactional
    void getAllProductsByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where barcode equals to
        defaultProductFiltering("barcode.equals=" + DEFAULT_BARCODE, "barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllProductsByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where barcode in
        defaultProductFiltering("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE, "barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllProductsByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where barcode is not null
        defaultProductFiltering("barcode.specified=true", "barcode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByBarcodeContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where barcode contains
        defaultProductFiltering("barcode.contains=" + DEFAULT_BARCODE, "barcode.contains=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllProductsByBarcodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where barcode does not contain
        defaultProductFiltering("barcode.doesNotContain=" + UPDATED_BARCODE, "barcode.doesNotContain=" + DEFAULT_BARCODE);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name equals to
        defaultProductFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name in
        defaultProductFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name contains
        defaultProductFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain
        defaultProductFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where category equals to
        defaultProductFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where category in
        defaultProductFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where category is not null
        defaultProductFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCategoryContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where category contains
        defaultProductFiltering("category.contains=" + DEFAULT_CATEGORY, "category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where category does not contain
        defaultProductFiltering("category.doesNotContain=" + UPDATED_CATEGORY, "category.doesNotContain=" + DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByShapeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where shape equals to
        defaultProductFiltering("shape.equals=" + DEFAULT_SHAPE, "shape.equals=" + UPDATED_SHAPE);
    }

    @Test
    @Transactional
    void getAllProductsByShapeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where shape in
        defaultProductFiltering("shape.in=" + DEFAULT_SHAPE + "," + UPDATED_SHAPE, "shape.in=" + UPDATED_SHAPE);
    }

    @Test
    @Transactional
    void getAllProductsByShapeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where shape is not null
        defaultProductFiltering("shape.specified=true", "shape.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByShapeContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where shape contains
        defaultProductFiltering("shape.contains=" + DEFAULT_SHAPE, "shape.contains=" + UPDATED_SHAPE);
    }

    @Test
    @Transactional
    void getAllProductsByShapeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where shape does not contain
        defaultProductFiltering("shape.doesNotContain=" + UPDATED_SHAPE, "shape.doesNotContain=" + DEFAULT_SHAPE);
    }

    @Test
    @Transactional
    void getAllProductsByRetailPackIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where retailPack equals to
        defaultProductFiltering("retailPack.equals=" + DEFAULT_RETAIL_PACK, "retailPack.equals=" + UPDATED_RETAIL_PACK);
    }

    @Test
    @Transactional
    void getAllProductsByRetailPackIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where retailPack in
        defaultProductFiltering("retailPack.in=" + DEFAULT_RETAIL_PACK + "," + UPDATED_RETAIL_PACK, "retailPack.in=" + UPDATED_RETAIL_PACK);
    }

    @Test
    @Transactional
    void getAllProductsByRetailPackIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where retailPack is not null
        defaultProductFiltering("retailPack.specified=true", "retailPack.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByRetailPackIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where retailPack is greater than or equal to
        defaultProductFiltering(
            "retailPack.greaterThanOrEqual=" + DEFAULT_RETAIL_PACK,
            "retailPack.greaterThanOrEqual=" + UPDATED_RETAIL_PACK
        );
    }

    @Test
    @Transactional
    void getAllProductsByRetailPackIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where retailPack is less than or equal to
        defaultProductFiltering("retailPack.lessThanOrEqual=" + DEFAULT_RETAIL_PACK, "retailPack.lessThanOrEqual=" + SMALLER_RETAIL_PACK);
    }

    @Test
    @Transactional
    void getAllProductsByRetailPackIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where retailPack is less than
        defaultProductFiltering("retailPack.lessThan=" + UPDATED_RETAIL_PACK, "retailPack.lessThan=" + DEFAULT_RETAIL_PACK);
    }

    @Test
    @Transactional
    void getAllProductsByRetailPackIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where retailPack is greater than
        defaultProductFiltering("retailPack.greaterThan=" + SMALLER_RETAIL_PACK, "retailPack.greaterThan=" + DEFAULT_RETAIL_PACK);
    }

    @Test
    @Transactional
    void getAllProductsByWholesalePackIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where wholesalePack equals to
        defaultProductFiltering("wholesalePack.equals=" + DEFAULT_WHOLESALE_PACK, "wholesalePack.equals=" + UPDATED_WHOLESALE_PACK);
    }

    @Test
    @Transactional
    void getAllProductsByWholesalePackIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where wholesalePack in
        defaultProductFiltering(
            "wholesalePack.in=" + DEFAULT_WHOLESALE_PACK + "," + UPDATED_WHOLESALE_PACK,
            "wholesalePack.in=" + UPDATED_WHOLESALE_PACK
        );
    }

    @Test
    @Transactional
    void getAllProductsByWholesalePackIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where wholesalePack is not null
        defaultProductFiltering("wholesalePack.specified=true", "wholesalePack.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByWholesalePackIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where wholesalePack is greater than or equal to
        defaultProductFiltering(
            "wholesalePack.greaterThanOrEqual=" + DEFAULT_WHOLESALE_PACK,
            "wholesalePack.greaterThanOrEqual=" + UPDATED_WHOLESALE_PACK
        );
    }

    @Test
    @Transactional
    void getAllProductsByWholesalePackIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where wholesalePack is less than or equal to
        defaultProductFiltering(
            "wholesalePack.lessThanOrEqual=" + DEFAULT_WHOLESALE_PACK,
            "wholesalePack.lessThanOrEqual=" + SMALLER_WHOLESALE_PACK
        );
    }

    @Test
    @Transactional
    void getAllProductsByWholesalePackIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where wholesalePack is less than
        defaultProductFiltering("wholesalePack.lessThan=" + UPDATED_WHOLESALE_PACK, "wholesalePack.lessThan=" + DEFAULT_WHOLESALE_PACK);
    }

    @Test
    @Transactional
    void getAllProductsByWholesalePackIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where wholesalePack is greater than
        defaultProductFiltering(
            "wholesalePack.greaterThan=" + SMALLER_WHOLESALE_PACK,
            "wholesalePack.greaterThan=" + DEFAULT_WHOLESALE_PACK
        );
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description equals to
        defaultProductFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description in
        defaultProductFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description contains
        defaultProductFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain
        defaultProductFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByStockQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockQuantity equals to
        defaultProductFiltering("stockQuantity.equals=" + DEFAULT_STOCK_QUANTITY, "stockQuantity.equals=" + UPDATED_STOCK_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByStockQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockQuantity in
        defaultProductFiltering(
            "stockQuantity.in=" + DEFAULT_STOCK_QUANTITY + "," + UPDATED_STOCK_QUANTITY,
            "stockQuantity.in=" + UPDATED_STOCK_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByStockQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockQuantity is not null
        defaultProductFiltering("stockQuantity.specified=true", "stockQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByStockQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockQuantity is greater than or equal to
        defaultProductFiltering(
            "stockQuantity.greaterThanOrEqual=" + DEFAULT_STOCK_QUANTITY,
            "stockQuantity.greaterThanOrEqual=" + UPDATED_STOCK_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByStockQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockQuantity is less than or equal to
        defaultProductFiltering(
            "stockQuantity.lessThanOrEqual=" + DEFAULT_STOCK_QUANTITY,
            "stockQuantity.lessThanOrEqual=" + SMALLER_STOCK_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByStockQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockQuantity is less than
        defaultProductFiltering("stockQuantity.lessThan=" + UPDATED_STOCK_QUANTITY, "stockQuantity.lessThan=" + DEFAULT_STOCK_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByStockQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockQuantity is greater than
        defaultProductFiltering(
            "stockQuantity.greaterThan=" + SMALLER_STOCK_QUANTITY,
            "stockQuantity.greaterThan=" + DEFAULT_STOCK_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByLowStockAlertIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lowStockAlert equals to
        defaultProductFiltering("lowStockAlert.equals=" + DEFAULT_LOW_STOCK_ALERT, "lowStockAlert.equals=" + UPDATED_LOW_STOCK_ALERT);
    }

    @Test
    @Transactional
    void getAllProductsByLowStockAlertIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lowStockAlert in
        defaultProductFiltering(
            "lowStockAlert.in=" + DEFAULT_LOW_STOCK_ALERT + "," + UPDATED_LOW_STOCK_ALERT,
            "lowStockAlert.in=" + UPDATED_LOW_STOCK_ALERT
        );
    }

    @Test
    @Transactional
    void getAllProductsByLowStockAlertIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lowStockAlert is not null
        defaultProductFiltering("lowStockAlert.specified=true", "lowStockAlert.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByLowStockAlertIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lowStockAlert is greater than or equal to
        defaultProductFiltering(
            "lowStockAlert.greaterThanOrEqual=" + DEFAULT_LOW_STOCK_ALERT,
            "lowStockAlert.greaterThanOrEqual=" + UPDATED_LOW_STOCK_ALERT
        );
    }

    @Test
    @Transactional
    void getAllProductsByLowStockAlertIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lowStockAlert is less than or equal to
        defaultProductFiltering(
            "lowStockAlert.lessThanOrEqual=" + DEFAULT_LOW_STOCK_ALERT,
            "lowStockAlert.lessThanOrEqual=" + SMALLER_LOW_STOCK_ALERT
        );
    }

    @Test
    @Transactional
    void getAllProductsByLowStockAlertIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lowStockAlert is less than
        defaultProductFiltering("lowStockAlert.lessThan=" + UPDATED_LOW_STOCK_ALERT, "lowStockAlert.lessThan=" + DEFAULT_LOW_STOCK_ALERT);
    }

    @Test
    @Transactional
    void getAllProductsByLowStockAlertIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lowStockAlert is greater than
        defaultProductFiltering(
            "lowStockAlert.greaterThan=" + SMALLER_LOW_STOCK_ALERT,
            "lowStockAlert.greaterThan=" + DEFAULT_LOW_STOCK_ALERT
        );
    }

    @Test
    @Transactional
    void getAllProductsByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where remarks equals to
        defaultProductFiltering("remarks.equals=" + DEFAULT_REMARKS, "remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllProductsByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where remarks in
        defaultProductFiltering("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS, "remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllProductsByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where remarks is not null
        defaultProductFiltering("remarks.specified=true", "remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByRemarksContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where remarks contains
        defaultProductFiltering("remarks.contains=" + DEFAULT_REMARKS, "remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllProductsByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where remarks does not contain
        defaultProductFiltering("remarks.doesNotContain=" + UPDATED_REMARKS, "remarks.doesNotContain=" + DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void getAllProductsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where location equals to
        defaultProductFiltering("location.equals=" + DEFAULT_LOCATION, "location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllProductsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where location in
        defaultProductFiltering("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION, "location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllProductsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where location is not null
        defaultProductFiltering("location.specified=true", "location.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByLocationContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where location contains
        defaultProductFiltering("location.contains=" + DEFAULT_LOCATION, "location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllProductsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where location does not contain
        defaultProductFiltering("location.doesNotContain=" + UPDATED_LOCATION, "location.doesNotContain=" + DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void getAllProductsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where message equals to
        defaultProductFiltering("message.equals=" + DEFAULT_MESSAGE, "message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllProductsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where message in
        defaultProductFiltering("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE, "message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllProductsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where message is not null
        defaultProductFiltering("message.specified=true", "message.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where message contains
        defaultProductFiltering("message.contains=" + DEFAULT_MESSAGE, "message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllProductsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where message does not contain
        defaultProductFiltering("message.doesNotContain=" + UPDATED_MESSAGE, "message.doesNotContain=" + DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllProductsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where value equals to
        defaultProductFiltering("value.equals=" + DEFAULT_VALUE, "value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllProductsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where value in
        defaultProductFiltering("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE, "value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllProductsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where value is not null
        defaultProductFiltering("value.specified=true", "value.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByValueContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where value contains
        defaultProductFiltering("value.contains=" + DEFAULT_VALUE, "value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllProductsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where value does not contain
        defaultProductFiltering("value.doesNotContain=" + UPDATED_VALUE, "value.doesNotContain=" + DEFAULT_VALUE);
    }

    private void defaultProductFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductShouldBeFound(shouldBeFound);
        defaultProductShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].shape").value(hasItem(DEFAULT_SHAPE)))
            .andExpect(jsonPath("$.[*].retailPack").value(hasItem(DEFAULT_RETAIL_PACK)))
            .andExpect(jsonPath("$.[*].wholesalePack").value(hasItem(DEFAULT_WHOLESALE_PACK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].stockQuantity").value(hasItem(DEFAULT_STOCK_QUANTITY)))
            .andExpect(jsonPath("$.[*].lowStockAlert").value(hasItem(DEFAULT_LOW_STOCK_ALERT)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .sku(UPDATED_SKU)
            .barcode(UPDATED_BARCODE)
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .shape(UPDATED_SHAPE)
            .retailPack(UPDATED_RETAIL_PACK)
            .wholesalePack(UPDATED_WHOLESALE_PACK)
            .description(UPDATED_DESCRIPTION)
            .stockQuantity(UPDATED_STOCK_QUANTITY)
            .lowStockAlert(UPDATED_LOW_STOCK_ALERT)
            .remarks(UPDATED_REMARKS)
            .location(UPDATED_LOCATION)
            .message(UPDATED_MESSAGE)
            .value(UPDATED_VALUE);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductToMatchAllProperties(updatedProduct);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .sku(UPDATED_SKU)
            .name(UPDATED_NAME)
            .wholesalePack(UPDATED_WHOLESALE_PACK)
            .description(UPDATED_DESCRIPTION)
            .lowStockAlert(UPDATED_LOW_STOCK_ALERT)
            .message(UPDATED_MESSAGE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduct, product), getPersistedProduct(product));
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .sku(UPDATED_SKU)
            .barcode(UPDATED_BARCODE)
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .shape(UPDATED_SHAPE)
            .retailPack(UPDATED_RETAIL_PACK)
            .wholesalePack(UPDATED_WHOLESALE_PACK)
            .description(UPDATED_DESCRIPTION)
            .stockQuantity(UPDATED_STOCK_QUANTITY)
            .lowStockAlert(UPDATED_LOW_STOCK_ALERT)
            .remarks(UPDATED_REMARKS)
            .location(UPDATED_LOCATION)
            .message(UPDATED_MESSAGE)
            .value(UPDATED_VALUE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(partialUpdatedProduct, getPersistedProduct(partialUpdatedProduct));
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Product getPersistedProduct(Product product) {
        return productRepository.findById(product.getId()).orElseThrow();
    }

    protected void assertPersistedProductToMatchAllProperties(Product expectedProduct) {
        assertProductAllPropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }

    protected void assertPersistedProductToMatchUpdatableProperties(Product expectedProduct) {
        assertProductAllUpdatablePropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }
}
