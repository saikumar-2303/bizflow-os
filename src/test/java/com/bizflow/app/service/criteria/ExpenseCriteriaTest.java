package com.bizflow.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ExpenseCriteriaTest {

    @Test
    void newExpenseCriteriaHasAllFiltersNullTest() {
        var expenseCriteria = new ExpenseCriteria();
        assertThat(expenseCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void expenseCriteriaFluentMethodsCreatesFiltersTest() {
        var expenseCriteria = new ExpenseCriteria();

        setAllFilters(expenseCriteria);

        assertThat(expenseCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void expenseCriteriaCopyCreatesNullFilterTest() {
        var expenseCriteria = new ExpenseCriteria();
        var copy = expenseCriteria.copy();

        assertThat(expenseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(expenseCriteria)
        );
    }

    @Test
    void expenseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var expenseCriteria = new ExpenseCriteria();
        setAllFilters(expenseCriteria);

        var copy = expenseCriteria.copy();

        assertThat(expenseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(expenseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var expenseCriteria = new ExpenseCriteria();

        assertThat(expenseCriteria).hasToString("ExpenseCriteria{}");
    }

    private static void setAllFilters(ExpenseCriteria expenseCriteria) {
        expenseCriteria.id();
        expenseCriteria.title();
        expenseCriteria.category();
        expenseCriteria.amount();
        expenseCriteria.expenseDate();
        expenseCriteria.remarks();
        expenseCriteria.employeeId();
        expenseCriteria.distinct();
    }

    private static Condition<ExpenseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getExpenseDate()) &&
                condition.apply(criteria.getRemarks()) &&
                condition.apply(criteria.getEmployeeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExpenseCriteria> copyFiltersAre(ExpenseCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getExpenseDate(), copy.getExpenseDate()) &&
                condition.apply(criteria.getRemarks(), copy.getRemarks()) &&
                condition.apply(criteria.getEmployeeId(), copy.getEmployeeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
