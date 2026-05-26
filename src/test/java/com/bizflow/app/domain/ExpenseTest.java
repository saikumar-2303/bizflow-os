package com.bizflow.app.domain;

import static com.bizflow.app.domain.EmployeeTestSamples.*;
import static com.bizflow.app.domain.ExpenseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bizflow.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpenseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Expense.class);
        Expense expense1 = getExpenseSample1();
        Expense expense2 = new Expense();
        assertThat(expense1).isNotEqualTo(expense2);

        expense2.setId(expense1.getId());
        assertThat(expense1).isEqualTo(expense2);

        expense2 = getExpenseSample2();
        assertThat(expense1).isNotEqualTo(expense2);
    }

    @Test
    void employeeTest() {
        Expense expense = getExpenseRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        expense.setEmployee(employeeBack);
        assertThat(expense.getEmployee()).isEqualTo(employeeBack);

        expense.employee(null);
        assertThat(expense.getEmployee()).isNull();
    }
}
