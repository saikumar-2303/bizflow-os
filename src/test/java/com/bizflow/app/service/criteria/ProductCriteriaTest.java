package com.bizflow.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductCriteriaTest {

    @Test
    void newProductCriteriaHasAllFiltersNullTest() {
        var productCriteria = new ProductCriteria();
        assertThat(productCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productCriteriaFluentMethodsCreatesFiltersTest() {
        var productCriteria = new ProductCriteria();

        setAllFilters(productCriteria);

        assertThat(productCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productCriteriaCopyCreatesNullFilterTest() {
        var productCriteria = new ProductCriteria();
        var copy = productCriteria.copy();

        assertThat(productCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productCriteria)
        );
    }

    @Test
    void productCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productCriteria = new ProductCriteria();
        setAllFilters(productCriteria);

        var copy = productCriteria.copy();

        assertThat(productCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productCriteria = new ProductCriteria();

        assertThat(productCriteria).hasToString("ProductCriteria{}");
    }

    private static void setAllFilters(ProductCriteria productCriteria) {
        productCriteria.id();
        productCriteria.sku();
        productCriteria.barcode();
        productCriteria.name();
        productCriteria.category();
        productCriteria.shape();
        productCriteria.retailPack();
        productCriteria.wholesalePack();
        productCriteria.description();
        productCriteria.stockQuantity();
        productCriteria.lowStockAlert();
        productCriteria.remarks();
        productCriteria.location();
        productCriteria.message();
        productCriteria.value();
        productCriteria.distinct();
    }

    private static Condition<ProductCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSku()) &&
                condition.apply(criteria.getBarcode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getShape()) &&
                condition.apply(criteria.getRetailPack()) &&
                condition.apply(criteria.getWholesalePack()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getStockQuantity()) &&
                condition.apply(criteria.getLowStockAlert()) &&
                condition.apply(criteria.getRemarks()) &&
                condition.apply(criteria.getLocation()) &&
                condition.apply(criteria.getMessage()) &&
                condition.apply(criteria.getValue()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductCriteria> copyFiltersAre(ProductCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSku(), copy.getSku()) &&
                condition.apply(criteria.getBarcode(), copy.getBarcode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getShape(), copy.getShape()) &&
                condition.apply(criteria.getRetailPack(), copy.getRetailPack()) &&
                condition.apply(criteria.getWholesalePack(), copy.getWholesalePack()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getStockQuantity(), copy.getStockQuantity()) &&
                condition.apply(criteria.getLowStockAlert(), copy.getLowStockAlert()) &&
                condition.apply(criteria.getRemarks(), copy.getRemarks()) &&
                condition.apply(criteria.getLocation(), copy.getLocation()) &&
                condition.apply(criteria.getMessage(), copy.getMessage()) &&
                condition.apply(criteria.getValue(), copy.getValue()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
