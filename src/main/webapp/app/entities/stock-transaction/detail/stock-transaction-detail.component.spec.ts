import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { StockTransactionDetailComponent } from './stock-transaction-detail.component';

describe('StockTransaction Management Detail Component', () => {
  let comp: StockTransactionDetailComponent;
  let fixture: ComponentFixture<StockTransactionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockTransactionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./stock-transaction-detail.component').then(m => m.StockTransactionDetailComponent),
              resolve: { stockTransaction: () => of({ id: 10234 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StockTransactionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockTransactionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load stockTransaction on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StockTransactionDetailComponent);

      // THEN
      expect(instance.stockTransaction()).toEqual(expect.objectContaining({ id: 10234 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
