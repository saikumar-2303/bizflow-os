import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStockTransaction } from '../stock-transaction.model';
import { StockTransactionService } from '../service/stock-transaction.service';

@Component({
  templateUrl: './stock-transaction-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StockTransactionDeleteDialogComponent {
  stockTransaction?: IStockTransaction;

  protected stockTransactionService = inject(StockTransactionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stockTransactionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
