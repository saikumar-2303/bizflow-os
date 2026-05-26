package com.bizflow.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bizflow.app.domain.CustomerOrder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private String orderNumber;

    @NotNull
    private String status;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private Instant createdDate;

    private String remarks;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerOrderDTO)) {
            return false;
        }

        CustomerOrderDTO customerOrderDTO = (CustomerOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerOrderDTO{" +
            "id=" + getId() +
            ", orderNumber='" + getOrderNumber() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", customer=" + getCustomer() +
            "}";
    }
}
