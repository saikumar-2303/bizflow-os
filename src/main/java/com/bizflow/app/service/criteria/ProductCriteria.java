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

    private StringFilter sku;

    private StringFilter barcode;

    private StringFilter name;

    private StringFilter category;

    private StringFilter shape;

    private IntegerFilter retailPack;

    private IntegerFilter wholesalePack;

    private StringFilter description;

    private IntegerFilter stockQuantity;

    private IntegerFilter lowStockAlert;

    private StringFilter remarks;

    private StringFilter location;

    private StringFilter message;

    private StringFilter value;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sku = other.optionalSku().map(StringFilter::copy).orElse(null);
        this.barcode = other.optionalBarcode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(StringFilter::copy).orElse(null);
        this.shape = other.optionalShape().map(StringFilter::copy).orElse(null);
        this.retailPack = other.optionalRetailPack().map(IntegerFilter::copy).orElse(null);
        this.wholesalePack = other.optionalWholesalePack().map(IntegerFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.stockQuantity = other.optionalStockQuantity().map(IntegerFilter::copy).orElse(null);
        this.lowStockAlert = other.optionalLowStockAlert().map(IntegerFilter::copy).orElse(null);
        this.remarks = other.optionalRemarks().map(StringFilter::copy).orElse(null);
        this.location = other.optionalLocation().map(StringFilter::copy).orElse(null);
        this.message = other.optionalMessage().map(StringFilter::copy).orElse(null);
        this.value = other.optionalValue().map(StringFilter::copy).orElse(null);
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

    public StringFilter getSku() {
        return sku;
    }

    public Optional<StringFilter> optionalSku() {
        return Optional.ofNullable(sku);
    }

    public StringFilter sku() {
        if (sku == null) {
            setSku(new StringFilter());
        }
        return sku;
    }

    public void setSku(StringFilter sku) {
        this.sku = sku;
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

    public StringFilter getShape() {
        return shape;
    }

    public Optional<StringFilter> optionalShape() {
        return Optional.ofNullable(shape);
    }

    public StringFilter shape() {
        if (shape == null) {
            setShape(new StringFilter());
        }
        return shape;
    }

    public void setShape(StringFilter shape) {
        this.shape = shape;
    }

    public IntegerFilter getRetailPack() {
        return retailPack;
    }

    public Optional<IntegerFilter> optionalRetailPack() {
        return Optional.ofNullable(retailPack);
    }

    public IntegerFilter retailPack() {
        if (retailPack == null) {
            setRetailPack(new IntegerFilter());
        }
        return retailPack;
    }

    public void setRetailPack(IntegerFilter retailPack) {
        this.retailPack = retailPack;
    }

    public IntegerFilter getWholesalePack() {
        return wholesalePack;
    }

    public Optional<IntegerFilter> optionalWholesalePack() {
        return Optional.ofNullable(wholesalePack);
    }

    public IntegerFilter wholesalePack() {
        if (wholesalePack == null) {
            setWholesalePack(new IntegerFilter());
        }
        return wholesalePack;
    }

    public void setWholesalePack(IntegerFilter wholesalePack) {
        this.wholesalePack = wholesalePack;
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

    public StringFilter getRemarks() {
        return remarks;
    }

    public Optional<StringFilter> optionalRemarks() {
        return Optional.ofNullable(remarks);
    }

    public StringFilter remarks() {
        if (remarks == null) {
            setRemarks(new StringFilter());
        }
        return remarks;
    }

    public void setRemarks(StringFilter remarks) {
        this.remarks = remarks;
    }

    public StringFilter getLocation() {
        return location;
    }

    public Optional<StringFilter> optionalLocation() {
        return Optional.ofNullable(location);
    }

    public StringFilter location() {
        if (location == null) {
            setLocation(new StringFilter());
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getMessage() {
        return message;
    }

    public Optional<StringFilter> optionalMessage() {
        return Optional.ofNullable(message);
    }

    public StringFilter message() {
        if (message == null) {
            setMessage(new StringFilter());
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public StringFilter getValue() {
        return value;
    }

    public Optional<StringFilter> optionalValue() {
        return Optional.ofNullable(value);
    }

    public StringFilter value() {
        if (value == null) {
            setValue(new StringFilter());
        }
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
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
            Objects.equals(sku, that.sku) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(category, that.category) &&
            Objects.equals(shape, that.shape) &&
            Objects.equals(retailPack, that.retailPack) &&
            Objects.equals(wholesalePack, that.wholesalePack) &&
            Objects.equals(description, that.description) &&
            Objects.equals(stockQuantity, that.stockQuantity) &&
            Objects.equals(lowStockAlert, that.lowStockAlert) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(location, that.location) &&
            Objects.equals(message, that.message) &&
            Objects.equals(value, that.value) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sku,
            barcode,
            name,
            category,
            shape,
            retailPack,
            wholesalePack,
            description,
            stockQuantity,
            lowStockAlert,
            remarks,
            location,
            message,
            value,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSku().map(f -> "sku=" + f + ", ").orElse("") +
            optionalBarcode().map(f -> "barcode=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalShape().map(f -> "shape=" + f + ", ").orElse("") +
            optionalRetailPack().map(f -> "retailPack=" + f + ", ").orElse("") +
            optionalWholesalePack().map(f -> "wholesalePack=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStockQuantity().map(f -> "stockQuantity=" + f + ", ").orElse("") +
            optionalLowStockAlert().map(f -> "lowStockAlert=" + f + ", ").orElse("") +
            optionalRemarks().map(f -> "remarks=" + f + ", ").orElse("") +
            optionalLocation().map(f -> "location=" + f + ", ").orElse("") +
            optionalMessage().map(f -> "message=" + f + ", ").orElse("") +
            optionalValue().map(f -> "value=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
