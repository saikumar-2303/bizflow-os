package com.bizflow.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @NotNull
    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "shape")
    private String shape;

    @Column(name = "retail_pack")
    private Integer retailPack;

    @Column(name = "wholesale_pack")
    private Integer wholesalePack;

    @Column(name = "description")
    private String description;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "low_stock_alert")
    private Integer lowStockAlert;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "location")
    private String location;

    @Column(name = "message")
    private String message;

    @Column(name = "value")
    private String value;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return this.sku;
    }

    public Product sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public Product barcode(String barcode) {
        this.setBarcode(barcode);
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public Product category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShape() {
        return this.shape;
    }

    public Product shape(String shape) {
        this.setShape(shape);
        return this;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Integer getRetailPack() {
        return this.retailPack;
    }

    public Product retailPack(Integer retailPack) {
        this.setRetailPack(retailPack);
        return this;
    }

    public void setRetailPack(Integer retailPack) {
        this.retailPack = retailPack;
    }

    public Integer getWholesalePack() {
        return this.wholesalePack;
    }

    public Product wholesalePack(Integer wholesalePack) {
        this.setWholesalePack(wholesalePack);
        return this;
    }

    public void setWholesalePack(Integer wholesalePack) {
        this.wholesalePack = wholesalePack;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStockQuantity() {
        return this.stockQuantity;
    }

    public Product stockQuantity(Integer stockQuantity) {
        this.setStockQuantity(stockQuantity);
        return this;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getLowStockAlert() {
        return this.lowStockAlert;
    }

    public Product lowStockAlert(Integer lowStockAlert) {
        this.setLowStockAlert(lowStockAlert);
        return this;
    }

    public void setLowStockAlert(Integer lowStockAlert) {
        this.lowStockAlert = lowStockAlert;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Product remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLocation() {
        return this.location;
    }

    public Product location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMessage() {
        return this.message;
    }

    public Product message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return this.value;
    }

    public Product value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
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
