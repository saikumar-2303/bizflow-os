package com.bizflow.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Expense.
 */
@Entity
@Table(name = "expense")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Expense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "category")
    private String category;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "expense_date", nullable = false)
    private Instant expenseDate;

    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Expense id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Expense title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return this.category;
    }

    public Expense category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Expense amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getExpenseDate() {
        return this.expenseDate;
    }

    public Expense expenseDate(Instant expenseDate) {
        this.setExpenseDate(expenseDate);
        return this;
    }

    public void setExpenseDate(Instant expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Expense remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Expense employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expense)) {
            return false;
        }
        return getId() != null && getId().equals(((Expense) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Expense{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", category='" + getCategory() + "'" +
            ", amount=" + getAmount() +
            ", expenseDate='" + getExpenseDate() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
