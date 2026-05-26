package com.bizflow.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bizflow.app.domain.InvoiceItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal totalAmount;

    private ProductDTO product;

    private InvoiceDTO invoice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceItemDTO)) {
            return false;
        }

        InvoiceItemDTO invoiceItemDTO = (InvoiceItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", totalAmount=" + getTotalAmount() +
            ", product=" + getProduct() +
            ", invoice=" + getInvoice() +
            "}";
    }
}
