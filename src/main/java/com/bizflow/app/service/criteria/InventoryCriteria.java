package com.bizflow.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bizflow.app.domain.Inventory} entity. This class is used
 * in {@link com.bizflow.app.web.rest.InventoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter inventory_id;

    private StringFilter remarks;

    private StringFilter location;

    private StringFilter description;

    private LongFilter product_idId;

    private Boolean distinct;

    public InventoryCriteria() {}

    public InventoryCriteria(InventoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.inventory_id = other.optionalInventory_id().map(StringFilter::copy).orElse(null);
        this.remarks = other.optionalRemarks().map(StringFilter::copy).orElse(null);
        this.location = other.optionalLocation().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.product_idId = other.optionalProduct_idId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InventoryCriteria copy() {
        return new InventoryCriteria(this);
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

    public StringFilter getInventory_id() {
        return inventory_id;
    }

    public Optional<StringFilter> optionalInventory_id() {
        return Optional.ofNullable(inventory_id);
    }

    public StringFilter inventory_id() {
        if (inventory_id == null) {
            setInventory_id(new StringFilter());
        }
        return inventory_id;
    }

    public void setInventory_id(StringFilter inventory_id) {
        this.inventory_id = inventory_id;
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

    public LongFilter getProduct_idId() {
        return product_idId;
    }

    public Optional<LongFilter> optionalProduct_idId() {
        return Optional.ofNullable(product_idId);
    }

    public LongFilter product_idId() {
        if (product_idId == null) {
            setProduct_idId(new LongFilter());
        }
        return product_idId;
    }

    public void setProduct_idId(LongFilter product_idId) {
        this.product_idId = product_idId;
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
        final InventoryCriteria that = (InventoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(inventory_id, that.inventory_id) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(location, that.location) &&
            Objects.equals(description, that.description) &&
            Objects.equals(product_idId, that.product_idId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inventory_id, remarks, location, description, product_idId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalInventory_id().map(f -> "inventory_id=" + f + ", ").orElse("") +
            optionalRemarks().map(f -> "remarks=" + f + ", ").orElse("") +
            optionalLocation().map(f -> "location=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalProduct_idId().map(f -> "product_idId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
