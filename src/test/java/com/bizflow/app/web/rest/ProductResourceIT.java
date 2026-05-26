package com.bizflow.app.web.rest;

import static com.bizflow.app.domain.ProductAsserts.*;
import static com.bizflow.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bizflow.app.web.rest.TestUtil.sameNumber;
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
import java.math.BigDecimal;
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

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BUY_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BUY_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BUY_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SELL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SELL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_SELL_PRICE = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_STOCK_QUANTITY = 1;
    private static final Integer UPDATED_STOCK_QUANTITY = 2;
    private static final Integer SMALLER_STOCK_QUANTITY = 1 - 1;

    private static final Integer DEFAULT_LOW_STOCK_ALERT = 1;
    private static final Integer UPDATED_LOW_STOCK_ALERT = 2;
    private static final Integer SMALLER_LOW_STOCK_ALERT = 1 - 1;

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

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
            .name(DEFAULT_NAME)
            .category(DEFAULT_CATEGORY)
            .description(DEFAULT_DESCRIPTION)
            .buyPrice(DEFAULT_BUY_PRICE)
            .sellPrice(DEFAULT_SELL_PRICE)
            .stockQuantity(DEFAULT_STOCK_QUANTITY)
            .lowStockAlert(DEFAULT_LOW_STOCK_ALERT)
            .barcode(DEFAULT_BARCODE)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity() {
        return new Product()
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .buyPrice(UPDATED_BUY_PRICE)
            .sellPrice(UPDATED_SELL_PRICE)
            .stockQuantity(UPDATED_STOCK_QUANTITY)
            .lowStockAlert(UPDATED_LOW_STOCK_ALERT)
            .barcode(UPDATED_BARCODE)
            .active(UPDATED_ACTIVE);
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
    void checkBuyPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setBuyPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSellPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setSellPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setStockQuantity(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setActive(null);

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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].buyPrice").value(hasItem(sameNumber(DEFAULT_BUY_PRICE))))
            .andExpect(jsonPath("$.[*].sellPrice").value(hasItem(sameNumber(DEFAULT_SELL_PRICE))))
            .andExpect(jsonPath("$.[*].stockQuantity").value(hasItem(DEFAULT_STOCK_QUANTITY)))
            .andExpect(jsonPath("$.[*].lowStockAlert").value(hasItem(DEFAULT_LOW_STOCK_ALERT)))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.buyPrice").value(sameNumber(DEFAULT_BUY_PRICE)))
            .andExpect(jsonPath("$.sellPrice").value(sameNumber(DEFAULT_SELL_PRICE)))
            .andExpect(jsonPath("$.stockQuantity").value(DEFAULT_STOCK_QUANTITY))
            .andExpect(jsonPath("$.lowStockAlert").value(DEFAULT_LOW_STOCK_ALERT))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
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
    void getAllProductsByBuyPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where buyPrice equals to
        defaultProductFiltering("buyPrice.equals=" + DEFAULT_BUY_PRICE, "buyPrice.equals=" + UPDATED_BUY_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByBuyPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where buyPrice in
        defaultProductFiltering("buyPrice.in=" + DEFAULT_BUY_PRICE + "," + UPDATED_BUY_PRICE, "buyPrice.in=" + UPDATED_BUY_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByBuyPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where buyPrice is not null
        defaultProductFiltering("buyPrice.specified=true", "buyPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByBuyPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where buyPrice is greater than or equal to
        defaultProductFiltering("buyPrice.greaterThanOrEqual=" + DEFAULT_BUY_PRICE, "buyPrice.greaterThanOrEqual=" + UPDATED_BUY_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByBuyPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where buyPrice is less than or equal to
        defaultProductFiltering("buyPrice.lessThanOrEqual=" + DEFAULT_BUY_PRICE, "buyPrice.lessThanOrEqual=" + SMALLER_BUY_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByBuyPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where buyPrice is less than
        defaultProductFiltering("buyPrice.lessThan=" + UPDATED_BUY_PRICE, "buyPrice.lessThan=" + DEFAULT_BUY_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByBuyPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where buyPrice is greater than
        defaultProductFiltering("buyPrice.greaterThan=" + SMALLER_BUY_PRICE, "buyPrice.greaterThan=" + DEFAULT_BUY_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsBySellPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sellPrice equals to
        defaultProductFiltering("sellPrice.equals=" + DEFAULT_SELL_PRICE, "sellPrice.equals=" + UPDATED_SELL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsBySellPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sellPrice in
        defaultProductFiltering("sellPrice.in=" + DEFAULT_SELL_PRICE + "," + UPDATED_SELL_PRICE, "sellPrice.in=" + UPDATED_SELL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsBySellPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sellPrice is not null
        defaultProductFiltering("sellPrice.specified=true", "sellPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySellPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sellPrice is greater than or equal to
        defaultProductFiltering("sellPrice.greaterThanOrEqual=" + DEFAULT_SELL_PRICE, "sellPrice.greaterThanOrEqual=" + UPDATED_SELL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsBySellPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sellPrice is less than or equal to
        defaultProductFiltering("sellPrice.lessThanOrEqual=" + DEFAULT_SELL_PRICE, "sellPrice.lessThanOrEqual=" + SMALLER_SELL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsBySellPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sellPrice is less than
        defaultProductFiltering("sellPrice.lessThan=" + UPDATED_SELL_PRICE, "sellPrice.lessThan=" + DEFAULT_SELL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsBySellPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sellPrice is greater than
        defaultProductFiltering("sellPrice.greaterThan=" + SMALLER_SELL_PRICE, "sellPrice.greaterThan=" + DEFAULT_SELL_PRICE);
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
    void getAllProductsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where active equals to
        defaultProductFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllProductsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where active in
        defaultProductFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllProductsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where active is not null
        defaultProductFiltering("active.specified=true", "active.specified=false");
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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].buyPrice").value(hasItem(sameNumber(DEFAULT_BUY_PRICE))))
            .andExpect(jsonPath("$.[*].sellPrice").value(hasItem(sameNumber(DEFAULT_SELL_PRICE))))
            .andExpect(jsonPath("$.[*].stockQuantity").value(hasItem(DEFAULT_STOCK_QUANTITY)))
            .andExpect(jsonPath("$.[*].lowStockAlert").value(hasItem(DEFAULT_LOW_STOCK_ALERT)))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

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
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .buyPrice(UPDATED_BUY_PRICE)
            .sellPrice(UPDATED_SELL_PRICE)
            .stockQuantity(UPDATED_STOCK_QUANTITY)
            .lowStockAlert(UPDATED_LOW_STOCK_ALERT)
            .barcode(UPDATED_BARCODE)
            .active(UPDATED_ACTIVE);
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
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .buyPrice(UPDATED_BUY_PRICE)
            .sellPrice(UPDATED_SELL_PRICE)
            .stockQuantity(UPDATED_STOCK_QUANTITY)
            .lowStockAlert(UPDATED_LOW_STOCK_ALERT);

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
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .buyPrice(UPDATED_BUY_PRICE)
            .sellPrice(UPDATED_SELL_PRICE)
            .stockQuantity(UPDATED_STOCK_QUANTITY)
            .lowStockAlert(UPDATED_LOW_STOCK_ALERT)
            .barcode(UPDATED_BARCODE)
            .active(UPDATED_ACTIVE);

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
