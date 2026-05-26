package com.bizflow.app.web.rest;

import static com.bizflow.app.domain.StockTransactionAsserts.*;
import static com.bizflow.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bizflow.app.IntegrationTest;
import com.bizflow.app.domain.StockTransaction;
import com.bizflow.app.repository.StockTransactionRepository;
import com.bizflow.app.service.StockTransactionService;
import com.bizflow.app.service.dto.StockTransactionDTO;
import com.bizflow.app.service.mapper.StockTransactionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link StockTransactionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StockTransactionResourceIT {

    private static final String DEFAULT_TRANSACTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_PREVIOUS_STOCK = 1;
    private static final Integer UPDATED_PREVIOUS_STOCK = 2;

    private static final Integer DEFAULT_NEW_STOCK = 1;
    private static final Integer UPDATED_NEW_STOCK = 2;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/stock-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StockTransactionRepository stockTransactionRepository;

    @Mock
    private StockTransactionRepository stockTransactionRepositoryMock;

    @Autowired
    private StockTransactionMapper stockTransactionMapper;

    @Mock
    private StockTransactionService stockTransactionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockTransactionMockMvc;

    private StockTransaction stockTransaction;

    private StockTransaction insertedStockTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockTransaction createEntity() {
        return new StockTransaction()
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .quantity(DEFAULT_QUANTITY)
            .previousStock(DEFAULT_PREVIOUS_STOCK)
            .newStock(DEFAULT_NEW_STOCK)
            .remarks(DEFAULT_REMARKS)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockTransaction createUpdatedEntity() {
        return new StockTransaction()
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .quantity(UPDATED_QUANTITY)
            .previousStock(UPDATED_PREVIOUS_STOCK)
            .newStock(UPDATED_NEW_STOCK)
            .remarks(UPDATED_REMARKS)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        stockTransaction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStockTransaction != null) {
            stockTransactionRepository.delete(insertedStockTransaction);
            insertedStockTransaction = null;
        }
    }

    @Test
    @Transactional
    void createStockTransaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StockTransaction
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);
        var returnedStockTransactionDTO = om.readValue(
            restStockTransactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockTransactionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StockTransactionDTO.class
        );

        // Validate the StockTransaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStockTransaction = stockTransactionMapper.toEntity(returnedStockTransactionDTO);
        assertStockTransactionUpdatableFieldsEquals(returnedStockTransaction, getPersistedStockTransaction(returnedStockTransaction));

        insertedStockTransaction = returnedStockTransaction;
    }

    @Test
    @Transactional
    void createStockTransactionWithExistingId() throws Exception {
        // Create the StockTransaction with an existing ID
        stockTransaction.setId(1L);
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransactionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockTransaction.setTransactionType(null);

        // Create the StockTransaction, which fails.
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        restStockTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockTransaction.setQuantity(null);

        // Create the StockTransaction, which fails.
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        restStockTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPreviousStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockTransaction.setPreviousStock(null);

        // Create the StockTransaction, which fails.
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        restStockTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNewStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockTransaction.setNewStock(null);

        // Create the StockTransaction, which fails.
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        restStockTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockTransaction.setCreatedDate(null);

        // Create the StockTransaction, which fails.
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        restStockTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStockTransactions() throws Exception {
        // Initialize the database
        insertedStockTransaction = stockTransactionRepository.saveAndFlush(stockTransaction);

        // Get all the stockTransactionList
        restStockTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].previousStock").value(hasItem(DEFAULT_PREVIOUS_STOCK)))
            .andExpect(jsonPath("$.[*].newStock").value(hasItem(DEFAULT_NEW_STOCK)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStockTransactionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(stockTransactionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStockTransactionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(stockTransactionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStockTransactionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(stockTransactionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStockTransactionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(stockTransactionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStockTransaction() throws Exception {
        // Initialize the database
        insertedStockTransaction = stockTransactionRepository.saveAndFlush(stockTransaction);

        // Get the stockTransaction
        restStockTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, stockTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockTransaction.getId().intValue()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.previousStock").value(DEFAULT_PREVIOUS_STOCK))
            .andExpect(jsonPath("$.newStock").value(DEFAULT_NEW_STOCK))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStockTransaction() throws Exception {
        // Get the stockTransaction
        restStockTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockTransaction() throws Exception {
        // Initialize the database
        insertedStockTransaction = stockTransactionRepository.saveAndFlush(stockTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockTransaction
        StockTransaction updatedStockTransaction = stockTransactionRepository.findById(stockTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStockTransaction are not directly saved in db
        em.detach(updatedStockTransaction);
        updatedStockTransaction
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .quantity(UPDATED_QUANTITY)
            .previousStock(UPDATED_PREVIOUS_STOCK)
            .newStock(UPDATED_NEW_STOCK)
            .remarks(UPDATED_REMARKS)
            .createdDate(UPDATED_CREATED_DATE);
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(updatedStockTransaction);

        restStockTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the StockTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStockTransactionToMatchAllProperties(updatedStockTransaction);
    }

    @Test
    @Transactional
    void putNonExistingStockTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockTransaction.setId(longCount.incrementAndGet());

        // Create the StockTransaction
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockTransaction.setId(longCount.incrementAndGet());

        // Create the StockTransaction
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockTransaction.setId(longCount.incrementAndGet());

        // Create the StockTransaction
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockTransactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedStockTransaction = stockTransactionRepository.saveAndFlush(stockTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockTransaction using partial update
        StockTransaction partialUpdatedStockTransaction = new StockTransaction();
        partialUpdatedStockTransaction.setId(stockTransaction.getId());

        partialUpdatedStockTransaction
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .newStock(UPDATED_NEW_STOCK)
            .createdDate(UPDATED_CREATED_DATE);

        restStockTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockTransaction))
            )
            .andExpect(status().isOk());

        // Validate the StockTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockTransactionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStockTransaction, stockTransaction),
            getPersistedStockTransaction(stockTransaction)
        );
    }

    @Test
    @Transactional
    void fullUpdateStockTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedStockTransaction = stockTransactionRepository.saveAndFlush(stockTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockTransaction using partial update
        StockTransaction partialUpdatedStockTransaction = new StockTransaction();
        partialUpdatedStockTransaction.setId(stockTransaction.getId());

        partialUpdatedStockTransaction
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .quantity(UPDATED_QUANTITY)
            .previousStock(UPDATED_PREVIOUS_STOCK)
            .newStock(UPDATED_NEW_STOCK)
            .remarks(UPDATED_REMARKS)
            .createdDate(UPDATED_CREATED_DATE);

        restStockTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockTransaction))
            )
            .andExpect(status().isOk());

        // Validate the StockTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockTransactionUpdatableFieldsEquals(
            partialUpdatedStockTransaction,
            getPersistedStockTransaction(partialUpdatedStockTransaction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStockTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockTransaction.setId(longCount.incrementAndGet());

        // Create the StockTransaction
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockTransaction.setId(longCount.incrementAndGet());

        // Create the StockTransaction
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockTransaction.setId(longCount.incrementAndGet());

        // Create the StockTransaction
        StockTransactionDTO stockTransactionDTO = stockTransactionMapper.toDto(stockTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockTransactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stockTransactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockTransaction() throws Exception {
        // Initialize the database
        insertedStockTransaction = stockTransactionRepository.saveAndFlush(stockTransaction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stockTransaction
        restStockTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stockTransactionRepository.count();
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

    protected StockTransaction getPersistedStockTransaction(StockTransaction stockTransaction) {
        return stockTransactionRepository.findById(stockTransaction.getId()).orElseThrow();
    }

    protected void assertPersistedStockTransactionToMatchAllProperties(StockTransaction expectedStockTransaction) {
        assertStockTransactionAllPropertiesEquals(expectedStockTransaction, getPersistedStockTransaction(expectedStockTransaction));
    }

    protected void assertPersistedStockTransactionToMatchUpdatableProperties(StockTransaction expectedStockTransaction) {
        assertStockTransactionAllUpdatablePropertiesEquals(
            expectedStockTransaction,
            getPersistedStockTransaction(expectedStockTransaction)
        );
    }
}
