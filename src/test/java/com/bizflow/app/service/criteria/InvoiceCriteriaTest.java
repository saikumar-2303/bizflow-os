package com.bizflow.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceCriteriaTest {

    @Test
    void newInvoiceCriteriaHasAllFiltersNullTest() {
        var invoiceCriteria = new InvoiceCriteria();
        assertThat(invoiceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void invoiceCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceCriteria = new InvoiceCriteria();

        setAllFilters(invoiceCriteria);

        assertThat(invoiceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void invoiceCriteriaCopyCreatesNullFilterTest() {
        var invoiceCriteria = new InvoiceCriteria();
        var copy = invoiceCriteria.copy();

        assertThat(invoiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceCriteria)
        );
    }

    @Test
    void invoiceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceCriteria = new InvoiceCriteria();
        setAllFilters(invoiceCriteria);

        var copy = invoiceCriteria.copy();

        assertThat(invoiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceCriteria = new InvoiceCriteria();

        assertThat(invoiceCriteria).hasToString("InvoiceCriteria{}");
    }

    private static void setAllFilters(InvoiceCriteria invoiceCriteria) {
        invoiceCriteria.id();
        invoiceCriteria.invoiceNumber();
        invoiceCriteria.totalAmount();
        invoiceCriteria.paidAmount();
        invoiceCriteria.pendingAmount();
        invoiceCriteria.paymentStatus();
        invoiceCriteria.createdDate();
        invoiceCriteria.remarks();
        invoiceCriteria.customerId();
        invoiceCriteria.distinct();
    }

    private static Condition<InvoiceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getInvoiceNumber()) &&
                condition.apply(criteria.getTotalAmount()) &&
                condition.apply(criteria.getPaidAmount()) &&
                condition.apply(criteria.getPendingAmount()) &&
                condition.apply(criteria.getPaymentStatus()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getRemarks()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InvoiceCriteria> copyFiltersAre(InvoiceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getInvoiceNumber(), copy.getInvoiceNumber()) &&
                condition.apply(criteria.getTotalAmount(), copy.getTotalAmount()) &&
                condition.apply(criteria.getPaidAmount(), copy.getPaidAmount()) &&
                condition.apply(criteria.getPendingAmount(), copy.getPendingAmount()) &&
                condition.apply(criteria.getPaymentStatus(), copy.getPaymentStatus()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getRemarks(), copy.getRemarks()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
