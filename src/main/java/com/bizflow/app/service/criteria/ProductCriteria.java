package com.bizflow.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bizflow.app.domain.Product} entity. This class is used
 * in {@link com.bizflow.app.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter category;

    private StringFilter description;

    private BigDecimalFilter buyPrice;

    private BigDecimalFilter sellPrice;

    private IntegerFilter stockQuantity;

    private IntegerFilter lowStockAlert;

    private StringFilter barcode;

    private BooleanFilter active;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.buyPrice = other.optionalBuyPrice().map(BigDecimalFilter::copy).orElse(null);
        this.sellPrice = other.optionalSellPrice().map(BigDecimalFilter::copy).orElse(null);
        this.stockQuantity = other.optionalStockQuantity().map(IntegerFilter::copy).orElse(null);
        this.lowStockAlert = other.optionalLowStockAlert().map(IntegerFilter::copy).orElse(null);
        this.barcode = other.optionalBarcode().map(StringFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCategory() {
        return category;
    }

    public Optional<StringFilter> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public StringFilter category() {
        if (category == null) {
            setCategory(new StringFilter());
        }
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getBuyPrice() {
        return buyPrice;
    }

    public Optional<BigDecimalFilter> optionalBuyPrice() {
        return Optional.ofNullable(buyPrice);
    }

    public BigDecimalFilter buyPrice() {
        if (buyPrice == null) {
            setBuyPrice(new BigDecimalFilter());
        }
        return buyPrice;
    }

    public void setBuyPrice(BigDecimalFilter buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimalFilter getSellPrice() {
        return sellPrice;
    }

    public Optional<BigDecimalFilter> optionalSellPrice() {
        return Optional.ofNullable(sellPrice);
    }

    public BigDecimalFilter sellPrice() {
        if (sellPrice == null) {
            setSellPrice(new BigDecimalFilter());
        }
        return sellPrice;
    }

    public void setSellPrice(BigDecimalFilter sellPrice) {
        this.sellPrice = sellPrice;
    }

    public IntegerFilter getStockQuantity() {
        return stockQuantity;
    }

    public Optional<IntegerFilter> optionalStockQuantity() {
        return Optional.ofNullable(stockQuantity);
    }

    public IntegerFilter stockQuantity() {
        if (stockQuantity == null) {
            setStockQuantity(new IntegerFilter());
        }
        return stockQuantity;
    }

    public void setStockQuantity(IntegerFilter stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public IntegerFilter getLowStockAlert() {
        return lowStockAlert;
    }

    public Optional<IntegerFilter> optionalLowStockAlert() {
        return Optional.ofNullable(lowStockAlert);
    }

    public IntegerFilter lowStockAlert() {
        if (lowStockAlert == null) {
            setLowStockAlert(new IntegerFilter());
        }
        return lowStockAlert;
    }

    public void setLowStockAlert(IntegerFilter lowStockAlert) {
        this.lowStockAlert = lowStockAlert;
    }

    public StringFilter getBarcode() {
        return barcode;
    }

    public Optional<StringFilter> optionalBarcode() {
        return Optional.ofNullable(barcode);
    }

    public StringFilter barcode() {
        if (barcode == null) {
            setBarcode(new StringFilter());
        }
        return barcode;
    }

    public void setBarcode(StringFilter barcode) {
        this.barcode = barcode;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(category, that.category) &&
            Objects.equals(description, that.description) &&
            Objects.equals(buyPrice, that.buyPrice) &&
            Objects.equals(sellPrice, that.sellPrice) &&
            Objects.equals(stockQuantity, that.stockQuantity) &&
            Objects.equals(lowStockAlert, that.lowStockAlert) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(active, that.active) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, description, buyPrice, sellPrice, stockQuantity, lowStockAlert, barcode, active, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalBuyPrice().map(f -> "buyPrice=" + f + ", ").orElse("") +
            optionalSellPrice().map(f -> "sellPrice=" + f + ", ").orElse("") +
            optionalStockQuantity().map(f -> "stockQuantity=" + f + ", ").orElse("") +
            optionalLowStockAlert().map(f -> "lowStockAlert=" + f + ", ").orElse("") +
            optionalBarcode().map(f -> "barcode=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
