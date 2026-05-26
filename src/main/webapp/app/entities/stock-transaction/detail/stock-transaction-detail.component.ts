import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IStockTransaction } from '../stock-transaction.model';

@Component({
  selector: 'jhi-stock-transaction-detail',
  templateUrl: './stock-transaction-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class StockTransactionDetailComponent {
  stockTransaction = input<IStockTransaction | null>(null);

  previousState(): void {
    window.history.back();
  }
}
