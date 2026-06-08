package com.bizflow.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "product_bom")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductBom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity_required", nullable = false)
    private Long quantityRequired;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "finished_product_id")
    private Product finishedProduct;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "raw_material_id")
    private Product rawMaterial;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(Long quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Product getFinishedProduct() {
        return finishedProduct;
    }

    public void setFinishedProduct(Product finishedProduct) {
        this.finishedProduct = finishedProduct;
    }

    public Product getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(Product rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductBom)) return false;
        return id != null && id.equals(((ProductBom) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "ProductBom{" +
            "id=" +
            id +
            ", quantityRequired=" +
            quantityRequired +
            ", remarks='" +
            remarks +
            '\'' +
            ", isActive=" +
            isActive +
            '}'
        );
    }
}
