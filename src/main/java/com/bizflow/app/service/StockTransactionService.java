package com.bizflow.app.service;

import com.bizflow.app.domain.StockTransaction;
import com.bizflow.app.repository.StockTransactionRepository;
import com.bizflow.app.service.dto.StockTransactionDTO;
import com.bizflow.app.service.mapper.StockTransactionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bizflow.app.domain.StockTransaction}.
 */
@Service
@Transactional
public class StockTransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(StockTransactionService.class);

    private final StockTransactionRepository stockTransactionRepository;

    private final StockTransactionMapper stockTransactionMapper;

    public StockTransactionService(StockTransactionRepository stockTransactionRepository, StockTransactionMapper stockTransactionMapper) {
        this.stockTransactionRepository = stockTransactionRepository;
        this.stockTransactionMapper = stockTransactionMapper;
    }

    /**
     * Save a stockTransaction.
     *
     * @param stockTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    public StockTransactionDTO save(StockTransactionDTO stockTransactionDTO) {
        LOG.debug("Request to save StockTransaction : {}", stockTransactionDTO);
        StockTransaction stockTransaction = stockTransactionMapper.toEntity(stockTransactionDTO);
        stockTransaction = stockTransactionRepository.save(stockTransaction);
        return stockTransactionMapper.toDto(stockTransaction);
    }

    /**
     * Update a stockTransaction.
     *
     * @param stockTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    public StockTransactionDTO update(StockTransactionDTO stockTransactionDTO) {
        LOG.debug("Request to update StockTransaction : {}", stockTransactionDTO);
        StockTransaction stockTransaction = stockTransactionMapper.toEntity(stockTransactionDTO);
        stockTransaction = stockTransactionRepository.save(stockTransaction);
        return stockTransactionMapper.toDto(stockTransaction);
    }

    /**
     * Partially update a stockTransaction.
     *
     * @param stockTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StockTransactionDTO> partialUpdate(StockTransactionDTO stockTransactionDTO) {
        LOG.debug("Request to partially update StockTransaction : {}", stockTransactionDTO);

        return stockTransactionRepository
            .findById(stockTransactionDTO.getId())
            .map(existingStockTransaction -> {
                stockTransactionMapper.partialUpdate(existingStockTransaction, stockTransactionDTO);

                return existingStockTransaction;
            })
            .map(stockTransactionRepository::save)
            .map(stockTransactionMapper::toDto);
    }

    /**
     * Get all the stockTransactions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StockTransactionDTO> findAll() {
        LOG.debug("Request to get all StockTransactions");
        return stockTransactionRepository
            .findAll()
            .stream()
            .map(stockTransactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the stockTransactions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<StockTransactionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return stockTransactionRepository.findAllWithEagerRelationships(pageable).map(stockTransactionMapper::toDto);
    }

    /**
     * Get one stockTransaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StockTransactionDTO> findOne(Long id) {
        LOG.debug("Request to get StockTransaction : {}", id);
        return stockTransactionRepository.findOneWithEagerRelationships(id).map(stockTransactionMapper::toDto);
    }

    /**
     * Delete the stockTransaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete StockTransaction : {}", id);
        stockTransactionRepository.deleteById(id);
    }
}
