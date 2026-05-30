package com.bizflow.app.web.rest;

import static com.bizflow.app.domain.CustomerOrderAsserts.*;
import static com.bizflow.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bizflow.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bizflow.app.IntegrationTest;
import com.bizflow.app.domain.Customer;
import com.bizflow.app.domain.CustomerOrder;
import com.bizflow.app.repository.CustomerOrderRepository;
import com.bizflow.app.service.CustomerOrderService;
import com.bizflow.app.service.dto.CustomerOrderDTO;
import com.bizflow.app.service.mapper.CustomerOrderMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CustomerOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomerOrderResourceIT {

    private static final String DEFAULT_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Mock
    private CustomerOrderRepository customerOrderRepositoryMock;

    @Autowired
    private CustomerOrderMapper customerOrderMapper;

    @Mock
    private CustomerOrderService customerOrderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerOrderMockMvc;

    private CustomerOrder customerOrder;

    private CustomerOrder insertedCustomerOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerOrder createEntity() {
        return new CustomerOrder()
            .orderNumber(DEFAULT_ORDER_NUMBER)
            .status(DEFAULT_STATUS)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .remarks(DEFAULT_REMARKS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerOrder createUpdatedEntity() {
        return new CustomerOrder()
            .orderNumber(UPDATED_ORDER_NUMBER)
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .remarks(UPDATED_REMARKS);
    }

    @BeforeEach
    void initTest() {
        customerOrder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCustomerOrder != null) {
            customerOrderRepository.delete(insertedCustomerOrder);
            insertedCustomerOrder = null;
        }
    }

    @Test
    @Transactional
    void createCustomerOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);
        var returnedCustomerOrderDTO = om.readValue(
            restCustomerOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerOrderDTO.class
        );

        // Validate the CustomerOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerOrder = customerOrderMapper.toEntity(returnedCustomerOrderDTO);
        assertCustomerOrderUpdatableFieldsEquals(returnedCustomerOrder, getPersistedCustomerOrder(returnedCustomerOrder));

        insertedCustomerOrder = returnedCustomerOrder;
    }

    @Test
    @Transactional
    void createCustomerOrderWithExistingId() throws Exception {
        // Create the CustomerOrder with an existing ID
        customerOrder.setId(1L);
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerOrder.setOrderNumber(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        restCustomerOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerOrder.setStatus(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        restCustomerOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerOrder.setTotalAmount(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        restCustomerOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerOrder.setCreatedDate(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        restCustomerOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerOrders() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList
        restCustomerOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(customerOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customerOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(customerOrderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustomerOrder() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get the customerOrder
        restCustomerOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, customerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getCustomerOrdersByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        Long id = customerOrder.getId();

        defaultCustomerOrderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerOrderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerOrderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByOrderNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderNumber equals to
        defaultCustomerOrderFiltering("orderNumber.equals=" + DEFAULT_ORDER_NUMBER, "orderNumber.equals=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByOrderNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderNumber in
        defaultCustomerOrderFiltering(
            "orderNumber.in=" + DEFAULT_ORDER_NUMBER + "," + UPDATED_ORDER_NUMBER,
            "orderNumber.in=" + UPDATED_ORDER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByOrderNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderNumber is not null
        defaultCustomerOrderFiltering("orderNumber.specified=true", "orderNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByOrderNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderNumber contains
        defaultCustomerOrderFiltering("orderNumber.contains=" + DEFAULT_ORDER_NUMBER, "orderNumber.contains=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByOrderNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderNumber does not contain
        defaultCustomerOrderFiltering(
            "orderNumber.doesNotContain=" + UPDATED_ORDER_NUMBER,
            "orderNumber.doesNotContain=" + DEFAULT_ORDER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where status equals to
        defaultCustomerOrderFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where status in
        defaultCustomerOrderFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where status is not null
        defaultCustomerOrderFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where status contains
        defaultCustomerOrderFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where status does not contain
        defaultCustomerOrderFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where totalAmount equals to
        defaultCustomerOrderFiltering("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT, "totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where totalAmount in
        defaultCustomerOrderFiltering(
            "totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT,
            "totalAmount.in=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where totalAmount is not null
        defaultCustomerOrderFiltering("totalAmount.specified=true", "totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where totalAmount is greater than or equal to
        defaultCustomerOrderFiltering(
            "totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT,
            "totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where totalAmount is less than or equal to
        defaultCustomerOrderFiltering(
            "totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT,
            "totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where totalAmount is less than
        defaultCustomerOrderFiltering("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT, "totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where totalAmount is greater than
        defaultCustomerOrderFiltering("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT, "totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where createdDate equals to
        defaultCustomerOrderFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where createdDate in
        defaultCustomerOrderFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where createdDate is not null
        defaultCustomerOrderFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where remarks equals to
        defaultCustomerOrderFiltering("remarks.equals=" + DEFAULT_REMARKS, "remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where remarks in
        defaultCustomerOrderFiltering("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS, "remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where remarks is not null
        defaultCustomerOrderFiltering("remarks.specified=true", "remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByRemarksContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where remarks contains
        defaultCustomerOrderFiltering("remarks.contains=" + DEFAULT_REMARKS, "remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where remarks does not contain
        defaultCustomerOrderFiltering("remarks.doesNotContain=" + UPDATED_REMARKS, "remarks.doesNotContain=" + DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void getAllCustomerOrdersByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customerOrderRepository.saveAndFlush(customerOrder);
            customer = CustomerResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        customerOrder.setCustomer(customer);
        customerOrderRepository.saveAndFlush(customerOrder);
        Long customerId = customer.getId();
        // Get all the customerOrderList where customer equals to customerId
        defaultCustomerOrderShouldBeFound("customerId.equals=" + customerId);

        // Get all the customerOrderList where customer equals to (customerId + 1)
        defaultCustomerOrderShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultCustomerOrderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerOrderShouldBeFound(shouldBeFound);
        defaultCustomerOrderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerOrderShouldBeFound(String filter) throws Exception {
        restCustomerOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));

        // Check, that the count call also returns 1
        restCustomerOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerOrderShouldNotBeFound(String filter) throws Exception {
        restCustomerOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerOrder() throws Exception {
        // Get the customerOrder
        restCustomerOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerOrder() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerOrder
        CustomerOrder updatedCustomerOrder = customerOrderRepository.findById(customerOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerOrder are not directly saved in db
        em.detach(updatedCustomerOrder);
        updatedCustomerOrder
            .orderNumber(UPDATED_ORDER_NUMBER)
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .remarks(UPDATED_REMARKS);
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(updatedCustomerOrder);

        restCustomerOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerOrderToMatchAllProperties(updatedCustomerOrder);
    }

    @Test
    @Transactional
    void putNonExistingCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerOrderWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerOrder using partial update
        CustomerOrder partialUpdatedCustomerOrder = new CustomerOrder();
        partialUpdatedCustomerOrder.setId(customerOrder.getId());

        partialUpdatedCustomerOrder.orderNumber(UPDATED_ORDER_NUMBER).status(UPDATED_STATUS);

        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerOrder))
            )
            .andExpect(status().isOk());

        // Validate the CustomerOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerOrder, customerOrder),
            getPersistedCustomerOrder(customerOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerOrderWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerOrder using partial update
        CustomerOrder partialUpdatedCustomerOrder = new CustomerOrder();
        partialUpdatedCustomerOrder.setId(customerOrder.getId());

        partialUpdatedCustomerOrder
            .orderNumber(UPDATED_ORDER_NUMBER)
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .remarks(UPDATED_REMARKS);

        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerOrder))
            )
            .andExpect(status().isOk());

        // Validate the CustomerOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerOrderUpdatableFieldsEquals(partialUpdatedCustomerOrder, getPersistedCustomerOrder(partialUpdatedCustomerOrder));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerOrder() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerOrder
        restCustomerOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerOrderRepository.count();
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

    protected CustomerOrder getPersistedCustomerOrder(CustomerOrder customerOrder) {
        return customerOrderRepository.findById(customerOrder.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerOrderToMatchAllProperties(CustomerOrder expectedCustomerOrder) {
        assertCustomerOrderAllPropertiesEquals(expectedCustomerOrder, getPersistedCustomerOrder(expectedCustomerOrder));
    }

    protected void assertPersistedCustomerOrderToMatchUpdatableProperties(CustomerOrder expectedCustomerOrder) {
        assertCustomerOrderAllUpdatablePropertiesEquals(expectedCustomerOrder, getPersistedCustomerOrder(expectedCustomerOrder));
    }
}
