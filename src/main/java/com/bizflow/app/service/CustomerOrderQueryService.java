package com.bizflow.app.service;

import com.bizflow.app.domain.*; // for static metamodels
import com.bizflow.app.domain.CustomerOrder;
import com.bizflow.app.repository.CustomerOrderRepository;
import com.bizflow.app.service.criteria.CustomerOrderCriteria;
import com.bizflow.app.service.dto.CustomerOrderDTO;
import com.bizflow.app.service.mapper.CustomerOrderMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CustomerOrder} entities in the database.
 * The main input is a {@link CustomerOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustomerOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerOrderQueryService extends QueryService<CustomerOrder> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerOrderQueryService.class);

    private final CustomerOrderRepository customerOrderRepository;

    private final CustomerOrderMapper customerOrderMapper;

    public CustomerOrderQueryService(CustomerOrderRepository customerOrderRepository, CustomerOrderMapper customerOrderMapper) {
        this.customerOrderRepository = customerOrderRepository;
        this.customerOrderMapper = customerOrderMapper;
    }

    /**
     * Return a {@link Page} of {@link CustomerOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerOrderDTO> findByCriteria(CustomerOrderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerOrder> specification = createSpecification(criteria);
        return customerOrderRepository.findAll(specification, page).map(customerOrderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerOrderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustomerOrder> specification = createSpecification(criteria);
        return customerOrderRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerOrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerOrder> createSpecification(CustomerOrderCriteria criteria) {
        Specification<CustomerOrder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CustomerOrder_.id),
                buildStringSpecification(criteria.getOrderNumber(), CustomerOrder_.orderNumber),
                buildStringSpecification(criteria.getStatus(), CustomerOrder_.status),
                buildRangeSpecification(criteria.getTotalAmount(), CustomerOrder_.totalAmount),
                buildRangeSpecification(criteria.getCreatedDate(), CustomerOrder_.createdDate),
                buildStringSpecification(criteria.getRemarks(), CustomerOrder_.remarks),
                buildSpecification(criteria.getCustomerId(), root -> root.join(CustomerOrder_.customer, JoinType.LEFT).get(Customer_.id))
            );
        }
        return specification;
    }
}
