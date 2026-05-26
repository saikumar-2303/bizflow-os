import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExpense } from '../expense.model';
import { ExpenseService } from '../service/expense.service';

@Component({
  templateUrl: './expense-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExpenseDeleteDialogComponent {
  expense?: IExpense;

  protected expenseService = inject(ExpenseService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
