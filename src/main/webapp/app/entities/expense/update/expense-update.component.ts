import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IExpense } from '../expense.model';
import { ExpenseService } from '../service/expense.service';
import { ExpenseFormGroup, ExpenseFormService } from './expense-form.service';

@Component({
  selector: 'jhi-expense-update',
  templateUrl: './expense-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExpenseUpdateComponent implements OnInit {
  isSaving = false;
  expense: IExpense | null = null;

  employeesSharedCollection: IEmployee[] = [];

  protected expenseService = inject(ExpenseService);
  protected expenseFormService = inject(ExpenseFormService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExpenseFormGroup = this.expenseFormService.createExpenseFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expense }) => {
      this.expense = expense;
      if (expense) {
        this.updateForm(expense);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expense = this.expenseFormService.getExpense(this.editForm);
    if (expense.id !== null) {
      this.subscribeToSaveResponse(this.expenseService.update(expense));
    } else {
      this.subscribeToSaveResponse(this.expenseService.create(expense));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpense>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(expense: IExpense): void {
    this.expense = expense;
    this.expenseFormService.resetForm(this.editForm, expense);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      expense.employee,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.expense?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
