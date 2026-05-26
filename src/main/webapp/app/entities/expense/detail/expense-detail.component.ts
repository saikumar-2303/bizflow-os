import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IExpense } from '../expense.model';

@Component({
  selector: 'jhi-expense-detail',
  templateUrl: './expense-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ExpenseDetailComponent {
  expense = input<IExpense | null>(null);

  previousState(): void {
    window.history.back();
  }
}
