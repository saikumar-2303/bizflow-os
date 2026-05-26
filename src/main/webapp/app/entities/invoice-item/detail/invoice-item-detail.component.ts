import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IInvoiceItem } from '../invoice-item.model';

@Component({
  selector: 'jhi-invoice-item-detail',
  templateUrl: './invoice-item-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class InvoiceItemDetailComponent {
  invoiceItem = input<IInvoiceItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
