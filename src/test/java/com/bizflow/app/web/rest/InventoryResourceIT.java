package com.bizflow.app.web.rest;

import static com.bizflow.app.domain.InventoryAsserts.*;
import static com.bizflow.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bizflow.app.IntegrationTest;
import com.bizflow.app.domain.Inventory;
import com.bizflow.app.domain.Product;
import com.bizflow.app.repository.InventoryRepository;
import com.bizflow.app.service.InventoryService;
import com.bizflow.app.service.dto.InventoryDTO;
import com.bizflow.app.service.mapper.InventoryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link InventoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InventoryResourceIT {

    private static final String DEFAULT_INVENTORY_ID = "AAAAAAAAAA";
    private static final String UPDATED_INVENTORY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryRepository inventoryRepositoryMock;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Mock
    private InventoryService inventoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryMockMvc;

    private Inventory inventory;

    private Inventory insertedInventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createEntity(EntityManager em) {
        Inventory inventory = new Inventory()
            .inventory_id(DEFAULT_INVENTORY_ID)
            .remarks(DEFAULT_REMARKS)
            .location(DEFAULT_LOCATION)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        inventory.setProduct_id(product);
        return inventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createUpdatedEntity(EntityManager em) {
        Inventory updatedInventory = new Inventory()
            .inventory_id(UPDATED_INVENTORY_ID)
            .remarks(UPDATED_REMARKS)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        updatedInventory.setProduct_id(product);
        return updatedInventory;
    }

    @BeforeEach
    void initTest() {
        inventory = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInventory != null) {
            inventoryRepository.delete(insertedInventory);
            insertedInventory = null;
        }
    }

    @Test
    @Transactional
    void createInventory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);
        var returnedInventoryDTO = om.readValue(
            restInventoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InventoryDTO.class
        );

        // Validate the Inventory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInventory = inventoryMapper.toEntity(returnedInventoryDTO);
        assertInventoryUpdatableFieldsEquals(returnedInventory, getPersistedInventory(returnedInventory));

        insertedInventory = returnedInventory;
    }

    @Test
    @Transactional
    void createInventoryWithExistingId() throws Exception {
        // Create the Inventory with an existing ID
        inventory.setId(1L);
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInventory_idIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventory.setInventory_id(null);

        // Create the Inventory, which fails.
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInventories() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].inventory_id").value(hasItem(DEFAULT_INVENTORY_ID)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(inventoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInventoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inventoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inventoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInventoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inventoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInventory() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get the inventory
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL_ID, inventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventory.getId().intValue()))
            .andExpect(jsonPath("$.inventory_id").value(DEFAULT_INVENTORY_ID))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getInventoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        Long id = inventory.getId();

        defaultInventoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInventoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInventoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInventoriesByInventory_idIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where inventory_id equals to
        defaultInventoryFiltering("inventory_id.equals=" + DEFAULT_INVENTORY_ID, "inventory_id.equals=" + UPDATED_INVENTORY_ID);
    }

    @Test
    @Transactional
    void getAllInventoriesByInventory_idIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where inventory_id in
        defaultInventoryFiltering(
            "inventory_id.in=" + DEFAULT_INVENTORY_ID + "," + UPDATED_INVENTORY_ID,
            "inventory_id.in=" + UPDATED_INVENTORY_ID
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByInventory_idIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where inventory_id is not null
        defaultInventoryFiltering("inventory_id.specified=true", "inventory_id.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByInventory_idContainsSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where inventory_id contains
        defaultInventoryFiltering("inventory_id.contains=" + DEFAULT_INVENTORY_ID, "inventory_id.contains=" + UPDATED_INVENTORY_ID);
    }

    @Test
    @Transactional
    void getAllInventoriesByInventory_idNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where inventory_id does not contain
        defaultInventoryFiltering(
            "inventory_id.doesNotContain=" + UPDATED_INVENTORY_ID,
            "inventory_id.doesNotContain=" + DEFAULT_INVENTORY_ID
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where remarks equals to
        defaultInventoryFiltering("remarks.equals=" + DEFAULT_REMARKS, "remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllInventoriesByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where remarks in
        defaultInventoryFiltering("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS, "remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllInventoriesByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where remarks is not null
        defaultInventoryFiltering("remarks.specified=true", "remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByRemarksContainsSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where remarks contains
        defaultInventoryFiltering("remarks.contains=" + DEFAULT_REMARKS, "remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllInventoriesByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where remarks does not contain
        defaultInventoryFiltering("remarks.doesNotContain=" + UPDATED_REMARKS, "remarks.doesNotContain=" + DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void getAllInventoriesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where location equals to
        defaultInventoryFiltering("location.equals=" + DEFAULT_LOCATION, "location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllInventoriesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where location in
        defaultInventoryFiltering("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION, "location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllInventoriesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where location is not null
        defaultInventoryFiltering("location.specified=true", "location.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByLocationContainsSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where location contains
        defaultInventoryFiltering("location.contains=" + DEFAULT_LOCATION, "location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllInventoriesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where location does not contain
        defaultInventoryFiltering("location.doesNotContain=" + UPDATED_LOCATION, "location.doesNotContain=" + DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void getAllInventoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where description equals to
        defaultInventoryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInventoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where description in
        defaultInventoryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where description is not null
        defaultInventoryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where description contains
        defaultInventoryFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInventoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where description does not contain
        defaultInventoryFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInventoriesByProduct_idIsEqualToSomething() throws Exception {
        Product product_id;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            inventoryRepository.saveAndFlush(inventory);
            product_id = ProductResourceIT.createEntity();
        } else {
            product_id = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product_id);
        em.flush();
        inventory.setProduct_id(product_id);
        inventoryRepository.saveAndFlush(inventory);
        Long product_idId = product_id.getId();
        // Get all the inventoryList where product_id equals to product_idId
        defaultInventoryShouldBeFound("product_idId.equals=" + product_idId);

        // Get all the inventoryList where product_id equals to (product_idId + 1)
        defaultInventoryShouldNotBeFound("product_idId.equals=" + (product_idId + 1));
    }

    private void defaultInventoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInventoryShouldBeFound(shouldBeFound);
        defaultInventoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInventoryShouldBeFound(String filter) throws Exception {
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].inventory_id").value(hasItem(DEFAULT_INVENTORY_ID)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInventoryShouldNotBeFound(String filter) throws Exception {
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInventory() throws Exception {
        // Get the inventory
        restInventoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventory() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventory
        Inventory updatedInventory = inventoryRepository.findById(inventory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInventory are not directly saved in db
        em.detach(updatedInventory);
        updatedInventory
            .inventory_id(UPDATED_INVENTORY_ID)
            .remarks(UPDATED_REMARKS)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION);
        InventoryDTO inventoryDTO = inventoryMapper.toDto(updatedInventory);

        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventoryToMatchAllProperties(updatedInventory);
    }

    @Test
    @Transactional
    void putNonExistingInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventory))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventory, inventory),
            getPersistedInventory(inventory)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        partialUpdatedInventory
            .inventory_id(UPDATED_INVENTORY_ID)
            .remarks(UPDATED_REMARKS)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION);

        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventory))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryUpdatableFieldsEquals(partialUpdatedInventory, getPersistedInventory(partialUpdatedInventory));
    }

    @Test
    @Transactional
    void patchNonExistingInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventory() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventory
        restInventoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventoryRepository.count();
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

    protected Inventory getPersistedInventory(Inventory inventory) {
        return inventoryRepository.findById(inventory.getId()).orElseThrow();
    }

    protected void assertPersistedInventoryToMatchAllProperties(Inventory expectedInventory) {
        assertInventoryAllPropertiesEquals(expectedInventory, getPersistedInventory(expectedInventory));
    }

    protected void assertPersistedInventoryToMatchUpdatableProperties(Inventory expectedInventory) {
        assertInventoryAllUpdatablePropertiesEquals(expectedInventory, getPersistedInventory(expectedInventory));
    }
}
