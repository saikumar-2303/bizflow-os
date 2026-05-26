package com.bizflow.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "designation")
    private String designation;

    @Column(name = "salary", precision = 21, scale = 2)
    private BigDecimal salary;

    @Column(name = "joining_date")
    private Instant joiningDate;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Employee name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public Employee phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Employee designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getSalary() {
        return this.salary;
    }

    public Employee salary(BigDecimal salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Instant getJoiningDate() {
        return this.joiningDate;
    }

    public Employee joiningDate(Instant joiningDate) {
        this.setJoiningDate(joiningDate);
        return this;
    }

    public void setJoiningDate(Instant joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Employee active(Boolean active) {
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
        if (!(o instanceof Employee)) {
            return false;
        }
        return getId() != null && getId().equals(((Employee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", salary=" + getSalary() +
            ", joiningDate='" + getJoiningDate() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
