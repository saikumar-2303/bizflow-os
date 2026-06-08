package com.bizflow.app.service;

import com.bizflow.app.domain.Product;
import com.bizflow.app.domain.Stock;
import com.bizflow.app.domain.StockTransaction;
import com.bizflow.app.repository.ProductRepository;
import com.bizflow.app.repository.StockTransactionRepository;
import com.bizflow.app.service.dto.Response;
import com.bizflow.app.service.dto.StockTransactionDTO;
import com.bizflow.app.service.mapper.StockTransactionMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.bizflow.app.domain.StockTransaction}.
 */
@Service
@Transactional
public class StockTransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(StockTransactionService.class);

    private final StockTransactionRepository stockTransactionRepository;

    private final StockTransactionMapper stockTransactionMapper;

    private final StockService stockService;

    private final ProductRepository productRepository;

    private final HelperService helperService;

    public StockTransactionService(
        StockTransactionRepository stockTransactionRepository,
        StockTransactionMapper stockTransactionMapper,
        StockService stockService,
        HelperService helperService,
        ProductRepository productRepository
    ) {
        this.stockTransactionRepository = stockTransactionRepository;
        this.stockTransactionMapper = stockTransactionMapper;
        this.stockService = stockService;
        this.helperService = helperService;
        this.productRepository = productRepository;
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

    // public Response addProducts(StockTransactionDTO dto) {

    //     Stock stock = stockService.getbyProductSKU(dto.getProduct().getSku())
    //             .orElseThrow(() -> new EntityNotFoundException("Stock not found"));

    //     StockTransaction stockTransaction = new StockTransaction();

    //     if (dto.getProduct() != null) {
    //         throw new EntityNotFoundException("Product not found");
    //     }

    //     stockTransaction.setProduct(dto.getProduct());

    //     stockTransaction.setPreviousStock(stock.getCurrentStock());

    //     stock.setAvailableStock(stock.getCurrentStock() + dto.getNewStock());
    //     stockService.save(stock);

    //     String productName = dto.getProduct().getName();

    //     switch (productName.toLowerCase()) {
    //         case "sheet":
    //             stockTransaction.setTransactionType("Sheet InWard");
    //             break;
    //         case "plate":
    //             stockTransaction.setTransactionType("Plate InWard");
    //             stockTransaction.setEmployee(dto.getEmployee());
    //             break;
    //         default:
    //             throw new IllegalArgumentException("Invalid product name: " + productName);
    //     }

    //     stockTransaction.setQuantity(stock.getCurrentStock());

    //     stockTransaction.setNewStock(dto.getNewStock());
    //     stockTransaction.setRemarks(dto.getRemarks());
    //     stockTransactionRepository.save(stockTransaction);
    //     return helperService.getResponse("Products added successfully", HttpStatus.OK);
    // }

    public Response addStock(String sku, Integer quantity) {
        Optional<Product> product = productRepository.findBySku(sku);

        Stock stock = stockService.getbyProductId(product.get().getId()).orElseThrow(() -> new EntityNotFoundException("Stock not found"));

        String productName = stock.getProduct().getName();

        StockTransaction transaction = new StockTransaction();

        String message = "";

        switch (productName.toLowerCase()) {
            case "sheet":
                transaction.setTransactionType("Sheet InWard");
                transaction.setPreviousStock(stock.getCurrentStock());
                transaction.setNewStock(quantity);
                stock.setCurrentStock(quantity + stock.getCurrentStock());
                stockService.save(stock);
                transaction.setQuantity(stock.getCurrentStock());
                message = "Raw Material saved successfully";
                break;
            case "plates":
                transaction.setTransactionType("Finished Plates InWard");
                // transaction.setEmployee(transaction.getEmployee());
                break;
            case "plate":
                transaction.setTransactionType("Plate InWard");
                transaction.setPreviousStock(stock.getCurrentStock());
                transaction.setNewStock(quantity);
                stock.setCurrentStock(quantity + stock.getCurrentStock());
                stockService.save(stock);

                transaction.setQuantity(stock.getCurrentStock());

                break;
            default:
                throw new IllegalArgumentException("Invalid product name: " + productName);
        }

        stockTransactionRepository.save(transaction);

        return helperService.getResponse(message, HttpStatus.OK);
    }
}
