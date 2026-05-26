package com.bizflow.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
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
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "buy_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal buyPrice;

    @NotNull
    @Column(name = "sell_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal sellPrice;

    @NotNull
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "low_stock_alert")
    private Integer lowStockAlert;

    @Column(name = "barcode")
    private String barcode;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

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

    public BigDecimal getBuyPrice() {
        return this.buyPrice;
    }

    public Product buyPrice(BigDecimal buyPrice) {
        this.setBuyPrice(buyPrice);
        return this;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellPrice() {
        return this.sellPrice;
    }

    public Product sellPrice(BigDecimal sellPrice) {
        this.setSellPrice(sellPrice);
        return this;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
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

    public Boolean getActive() {
        return this.active;
    }

    public Product active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
            ", name='" + getName() + "'" +
            ", category='" + getCategory() + "'" +
            ", description='" + getDescription() + "'" +
            ", buyPrice=" + getBuyPrice() +
            ", sellPrice=" + getSellPrice() +
            ", stockQuantity=" + getStockQuantity() +
            ", lowStockAlert=" + getLowStockAlert() +
            ", barcode='" + getBarcode() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
