package com.bizflow.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bizflow.app.domain.StockTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private String transactionType;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer previousStock;

    @NotNull
    private Integer newStock;

    private String remarks;

    @NotNull
    private Instant createdDate;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPreviousStock() {
        return previousStock;
    }

    public void setPreviousStock(Integer previousStock) {
        this.previousStock = previousStock;
    }

    public Integer getNewStock() {
        return newStock;
    }

    public void setNewStock(Integer newStock) {
        this.newStock = newStock;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockTransactionDTO)) {
            return false;
        }

        StockTransactionDTO stockTransactionDTO = (StockTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockTransactionDTO{" +
            "id=" + getId() +
            ", transactionType='" + getTransactionType() + "'" +
            ", quantity=" + getQuantity() +
            ", previousStock=" + getPreviousStock() +
            ", newStock=" + getNewStock() +
            ", remarks='" + getRemarks() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", product=" + getProduct() +
            "}";
    }
}
