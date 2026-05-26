package com.bizflow.app.web.rest;

import com.bizflow.app.repository.StockTransactionRepository;
import com.bizflow.app.service.StockTransactionService;
import com.bizflow.app.service.dto.StockTransactionDTO;
import com.bizflow.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bizflow.app.domain.StockTransaction}.
 */
@RestController
@RequestMapping("/api/stock-transactions")
public class StockTransactionResource {

    private static final Logger LOG = LoggerFactory.getLogger(StockTransactionResource.class);

    private static final String ENTITY_NAME = "stockTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockTransactionService stockTransactionService;

    private final StockTransactionRepository stockTransactionRepository;

    public StockTransactionResource(
        StockTransactionService stockTransactionService,
        StockTransactionRepository stockTransactionRepository
    ) {
        this.stockTransactionService = stockTransactionService;
        this.stockTransactionRepository = stockTransactionRepository;
    }

    /**
     * {@code POST  /stock-transactions} : Create a new stockTransaction.
     *
     * @param stockTransactionDTO the stockTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockTransactionDTO, or with status {@code 400 (Bad Request)} if the stockTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StockTransactionDTO> createStockTransaction(@Valid @RequestBody StockTransactionDTO stockTransactionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StockTransaction : {}", stockTransactionDTO);
        if (stockTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stockTransactionDTO = stockTransactionService.save(stockTransactionDTO);
        return ResponseEntity.created(new URI("/api/stock-transactions/" + stockTransactionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, stockTransactionDTO.getId().toString()))
            .body(stockTransactionDTO);
    }

    /**
     * {@code PUT  /stock-transactions/:id} : Updates an existing stockTransaction.
     *
     * @param id the id of the stockTransactionDTO to save.
     * @param stockTransactionDTO the stockTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the stockTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockTransactionDTO> updateStockTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StockTransactionDTO stockTransactionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StockTransaction : {}, {}", id, stockTransactionDTO);
        if (stockTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stockTransactionDTO = stockTransactionService.update(stockTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockTransactionDTO.getId().toString()))
            .body(stockTransactionDTO);
    }

    /**
     * {@code PATCH  /stock-transactions/:id} : Partial updates given fields of an existing stockTransaction, field will ignore if it is null
     *
     * @param id the id of the stockTransactionDTO to save.
     * @param stockTransactionDTO the stockTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the stockTransactionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stockTransactionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StockTransactionDTO> partialUpdateStockTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StockTransactionDTO stockTransactionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StockTransaction partially : {}, {}", id, stockTransactionDTO);
        if (stockTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockTransactionDTO> result = stockTransactionService.partialUpdate(stockTransactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockTransactionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stock-transactions} : get all the stockTransactions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockTransactions in body.
     */
    @GetMapping("")
    public List<StockTransactionDTO> getAllStockTransactions(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all StockTransactions");
        return stockTransactionService.findAll();
    }

    /**
     * {@code GET  /stock-transactions/:id} : get the "id" stockTransaction.
     *
     * @param id the id of the stockTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockTransactionDTO> getStockTransaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StockTransaction : {}", id);
        Optional<StockTransactionDTO> stockTransactionDTO = stockTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockTransactionDTO);
    }

    /**
     * {@code DELETE  /stock-transactions/:id} : delete the "id" stockTransaction.
     *
     * @param id the id of the stockTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockTransaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StockTransaction : {}", id);
        stockTransactionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
