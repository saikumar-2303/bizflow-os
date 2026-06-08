package com.bizflow.app.service.criteria;

import java.io.Serializable;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
public class StockCriteria implements Serializable, Criteria {

    private LongFilter id;

    private IntegerFilter currentStock;

    private IntegerFilter reservedStock;

    private IntegerFilter availableStock;

    private IntegerFilter minimumStock;

    private IntegerFilter reorderLevel;

    private StringFilter location;

    private StringFilter remarks;

    private InstantFilter lastUpdated;

    private LongFilter productId;

    private StringFilter productName;

    private Boolean distinct;

    public StockCriteria() {}

    public StockCriteria(StockCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.currentStock = other.currentStock == null ? null : other.currentStock.copy();
        this.reservedStock = other.reservedStock == null ? null : other.reservedStock.copy();
        this.availableStock = other.availableStock == null ? null : other.availableStock.copy();
        this.minimumStock = other.minimumStock == null ? null : other.minimumStock.copy();
        this.reorderLevel = other.reorderLevel == null ? null : other.reorderLevel.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.remarks = other.remarks == null ? null : other.remarks.copy();
        this.lastUpdated = other.lastUpdated == null ? null : other.lastUpdated.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.productName = other.productName == null ? null : other.productName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StockCriteria copy() {
        return new StockCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(IntegerFilter currentStock) {
        this.currentStock = currentStock;
    }

    public IntegerFilter getReservedStock() {
        return reservedStock;
    }

    public void setReservedStock(IntegerFilter reservedStock) {
        this.reservedStock = reservedStock;
    }

    public IntegerFilter getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(IntegerFilter availableStock) {
        this.availableStock = availableStock;
    }

    public IntegerFilter getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(IntegerFilter minimumStock) {
        this.minimumStock = minimumStock;
    }

    public IntegerFilter getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(IntegerFilter reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getRemarks() {
        return remarks;
    }

    public void setRemarks(StringFilter remarks) {
        this.remarks = remarks;
    }

    public InstantFilter getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(InstantFilter lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public StringFilter getProductName() {
        return productName;
    }

    public void setProductName(StringFilter productName) {
        this.productName = productName;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }
}
