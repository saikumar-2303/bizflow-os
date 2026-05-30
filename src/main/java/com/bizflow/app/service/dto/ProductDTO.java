package com.bizflow.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bizflow.app.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String sku;

    @NotNull
    private String barcode;

    @NotNull
    private String name;

    private String category;

    private String shape;

    private Integer retailPack;

    private Integer wholesalePack;

    private String description;

    private Integer stockQuantity;

    private Integer lowStockAlert;

    private String remarks;

    private String location;

    private String message;

    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Integer getRetailPack() {
        return retailPack;
    }

    public void setRetailPack(Integer retailPack) {
        this.retailPack = retailPack;
    }

    public Integer getWholesalePack() {
        return wholesalePack;
    }

    public void setWholesalePack(Integer wholesalePack) {
        this.wholesalePack = wholesalePack;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getLowStockAlert() {
        return lowStockAlert;
    }

    public void setLowStockAlert(Integer lowStockAlert) {
        this.lowStockAlert = lowStockAlert;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", barcode='" + getBarcode() + "'" +
            ", name='" + getName() + "'" +
            ", category='" + getCategory() + "'" +
            ", shape='" + getShape() + "'" +
            ", retailPack=" + getRetailPack() +
            ", wholesalePack=" + getWholesalePack() +
            ", description='" + getDescription() + "'" +
            ", stockQuantity=" + getStockQuantity() +
            ", lowStockAlert=" + getLowStockAlert() +
            ", remarks='" + getRemarks() + "'" +
            ", location='" + getLocation() + "'" +
            ", message='" + getMessage() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
