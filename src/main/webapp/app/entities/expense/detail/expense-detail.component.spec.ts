import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ExpenseDetailComponent } from './expense-detail.component';

describe('Expense Management Detail Component', () => {
  let comp: ExpenseDetailComponent;
  let fixture: ComponentFixture<ExpenseDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExpenseDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./expense-detail.component').then(m => m.ExpenseDetailComponent),
              resolve: { expense: () => of({ id: 17742 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ExpenseDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpenseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load expense on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ExpenseDetailComponent);

      // THEN
      expect(instance.expense()).toEqual(expect.objectContaining({ id: 17742 }));
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
