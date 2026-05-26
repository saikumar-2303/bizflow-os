package com.bizflow.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bizflow.app.domain.Invoice} entity. This class is used
 * in {@link com.bizflow.app.web.rest.InvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter invoiceNumber;

    private BigDecimalFilter totalAmount;

    private BigDecimalFilter paidAmount;

    private BigDecimalFilter pendingAmount;

    private StringFilter paymentStatus;

    private InstantFilter createdDate;

    private StringFilter remarks;

    private LongFilter customerId;

    private Boolean distinct;

    public InvoiceCriteria() {}

    public InvoiceCriteria(InvoiceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.invoiceNumber = other.optionalInvoiceNumber().map(StringFilter::copy).orElse(null);
        this.totalAmount = other.optionalTotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paidAmount = other.optionalPaidAmount().map(BigDecimalFilter::copy).orElse(null);
        this.pendingAmount = other.optionalPendingAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paymentStatus = other.optionalPaymentStatus().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.remarks = other.optionalRemarks().map(StringFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InvoiceCriteria copy() {
        return new InvoiceCriteria(this);
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

    public StringFilter getInvoiceNumber() {
        return invoiceNumber;
    }

    public Optional<StringFilter> optionalInvoiceNumber() {
        return Optional.ofNullable(invoiceNumber);
    }

    public StringFilter invoiceNumber() {
        if (invoiceNumber == null) {
            setInvoiceNumber(new StringFilter());
        }
        return invoiceNumber;
    }

    public void setInvoiceNumber(StringFilter invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public Optional<BigDecimalFilter> optionalTotalAmount() {
        return Optional.ofNullable(totalAmount);
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            setTotalAmount(new BigDecimalFilter());
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimalFilter getPaidAmount() {
        return paidAmount;
    }

    public Optional<BigDecimalFilter> optionalPaidAmount() {
        return Optional.ofNullable(paidAmount);
    }

    public BigDecimalFilter paidAmount() {
        if (paidAmount == null) {
            setPaidAmount(new BigDecimalFilter());
        }
        return paidAmount;
    }

    public void setPaidAmount(BigDecimalFilter paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimalFilter getPendingAmount() {
        return pendingAmount;
    }

    public Optional<BigDecimalFilter> optionalPendingAmount() {
        return Optional.ofNullable(pendingAmount);
    }

    public BigDecimalFilter pendingAmount() {
        if (pendingAmount == null) {
            setPendingAmount(new BigDecimalFilter());
        }
        return pendingAmount;
    }

    public void setPendingAmount(BigDecimalFilter pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public StringFilter getPaymentStatus() {
        return paymentStatus;
    }

    public Optional<StringFilter> optionalPaymentStatus() {
        return Optional.ofNullable(paymentStatus);
    }

    public StringFilter paymentStatus() {
        if (paymentStatus == null) {
            setPaymentStatus(new StringFilter());
        }
        return paymentStatus;
    }

    public void setPaymentStatus(StringFilter paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
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

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
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
        final InvoiceCriteria that = (InvoiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(invoiceNumber, that.invoiceNumber) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(paidAmount, that.paidAmount) &&
            Objects.equals(pendingAmount, that.pendingAmount) &&
            Objects.equals(paymentStatus, that.paymentStatus) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            invoiceNumber,
            totalAmount,
            paidAmount,
            pendingAmount,
            paymentStatus,
            createdDate,
            remarks,
            customerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalInvoiceNumber().map(f -> "invoiceNumber=" + f + ", ").orElse("") +
            optionalTotalAmount().map(f -> "totalAmount=" + f + ", ").orElse("") +
            optionalPaidAmount().map(f -> "paidAmount=" + f + ", ").orElse("") +
            optionalPendingAmount().map(f -> "pendingAmount=" + f + ", ").orElse("") +
            optionalPaymentStatus().map(f -> "paymentStatus=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalRemarks().map(f -> "remarks=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
