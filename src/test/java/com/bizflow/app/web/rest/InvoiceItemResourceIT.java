package com.bizflow.app.web.rest;

import static com.bizflow.app.domain.InvoiceItemAsserts.*;
import static com.bizflow.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bizflow.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bizflow.app.IntegrationTest;
import com.bizflow.app.domain.InvoiceItem;
import com.bizflow.app.repository.InvoiceItemRepository;
import com.bizflow.app.service.InvoiceItemService;
import com.bizflow.app.service.dto.InvoiceItemDTO;
import com.bizflow.app.service.mapper.InvoiceItemMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link InvoiceItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvoiceItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/invoice-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Mock
    private InvoiceItemRepository invoiceItemRepositoryMock;

    @Autowired
    private InvoiceItemMapper invoiceItemMapper;

    @Mock
    private InvoiceItemService invoiceItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceItemMockMvc;

    private InvoiceItem invoiceItem;

    private InvoiceItem insertedInvoiceItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItem createEntity() {
        return new InvoiceItem().quantity(DEFAULT_QUANTITY).price(DEFAULT_PRICE).totalAmount(DEFAULT_TOTAL_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItem createUpdatedEntity() {
        return new InvoiceItem().quantity(UPDATED_QUANTITY).price(UPDATED_PRICE).totalAmount(UPDATED_TOTAL_AMOUNT);
    }

    @BeforeEach
    void initTest() {
        invoiceItem = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInvoiceItem != null) {
            invoiceItemRepository.delete(insertedInvoiceItem);
            insertedInvoiceItem = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);
        var returnedInvoiceItemDTO = om.readValue(
            restInvoiceItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceItemDTO.class
        );

        // Validate the InvoiceItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInvoiceItem = invoiceItemMapper.toEntity(returnedInvoiceItemDTO);
        assertInvoiceItemUpdatableFieldsEquals(returnedInvoiceItem, getPersistedInvoiceItem(returnedInvoiceItem));

        insertedInvoiceItem = returnedInvoiceItem;
    }

    @Test
    @Transactional
    void createInvoiceItemWithExistingId() throws Exception {
        // Create the InvoiceItem with an existing ID
        invoiceItem.setId(1L);
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceItem.setQuantity(null);

        // Create the InvoiceItem, which fails.
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceItem.setPrice(null);

        // Create the InvoiceItem, which fails.
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceItem.setTotalAmount(null);

        // Create the InvoiceItem, which fails.
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceItems() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoiceItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(invoiceItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(invoiceItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoiceItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(invoiceItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(invoiceItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInvoiceItem() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get the invoiceItem
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceItem() throws Exception {
        // Get the invoiceItem
        restInvoiceItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceItem() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItem
        InvoiceItem updatedInvoiceItem = invoiceItemRepository.findById(invoiceItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceItem are not directly saved in db
        em.detach(updatedInvoiceItem);
        updatedInvoiceItem.quantity(UPDATED_QUANTITY).price(UPDATED_PRICE).totalAmount(UPDATED_TOTAL_AMOUNT);
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(updatedInvoiceItem);

        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceItemToMatchAllProperties(updatedInvoiceItem);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceItemWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItem using partial update
        InvoiceItem partialUpdatedInvoiceItem = new InvoiceItem();
        partialUpdatedInvoiceItem.setId(invoiceItem.getId());

        partialUpdatedInvoiceItem.quantity(UPDATED_QUANTITY).totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceItem, invoiceItem),
            getPersistedInvoiceItem(invoiceItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceItemWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItem using partial update
        InvoiceItem partialUpdatedInvoiceItem = new InvoiceItem();
        partialUpdatedInvoiceItem.setId(invoiceItem.getId());

        partialUpdatedInvoiceItem.quantity(UPDATED_QUANTITY).price(UPDATED_PRICE).totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceItemUpdatableFieldsEquals(partialUpdatedInvoiceItem, getPersistedInvoiceItem(partialUpdatedInvoiceItem));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceItem() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceItem
        restInvoiceItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceItemRepository.count();
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

    protected InvoiceItem getPersistedInvoiceItem(InvoiceItem invoiceItem) {
        return invoiceItemRepository.findById(invoiceItem.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceItemToMatchAllProperties(InvoiceItem expectedInvoiceItem) {
        assertInvoiceItemAllPropertiesEquals(expectedInvoiceItem, getPersistedInvoiceItem(expectedInvoiceItem));
    }

    protected void assertPersistedInvoiceItemToMatchUpdatableProperties(InvoiceItem expectedInvoiceItem) {
        assertInvoiceItemAllUpdatablePropertiesEquals(expectedInvoiceItem, getPersistedInvoiceItem(expectedInvoiceItem));
    }
}
