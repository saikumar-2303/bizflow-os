import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../stock-transaction.test-samples';

import { StockTransactionFormService } from './stock-transaction-form.service';

describe('StockTransaction Form Service', () => {
  let service: StockTransactionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StockTransactionFormService);
  });

  describe('Service methods', () => {
    describe('createStockTransactionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStockTransactionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            transactionType: expect.any(Object),
            quantity: expect.any(Object),
            previousStock: expect.any(Object),
            newStock: expect.any(Object),
            remarks: expect.any(Object),
            createdDate: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });

      it('passing IStockTransaction should create a new form with FormGroup', () => {
        const formGroup = service.createStockTransactionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            transactionType: expect.any(Object),
            quantity: expect.any(Object),
            previousStock: expect.any(Object),
            newStock: expect.any(Object),
            remarks: expect.any(Object),
            createdDate: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });
    });

    describe('getStockTransaction', () => {
      it('should return NewStockTransaction for default StockTransaction initial value', () => {
        const formGroup = service.createStockTransactionFormGroup(sampleWithNewData);

        const stockTransaction = service.getStockTransaction(formGroup) as any;

        expect(stockTransaction).toMatchObject(sampleWithNewData);
      });

      it('should return NewStockTransaction for empty StockTransaction initial value', () => {
        const formGroup = service.createStockTransactionFormGroup();

        const stockTransaction = service.getStockTransaction(formGroup) as any;

        expect(stockTransaction).toMatchObject({});
      });

      it('should return IStockTransaction', () => {
        const formGroup = service.createStockTransactionFormGroup(sampleWithRequiredData);

        const stockTransaction = service.getStockTransaction(formGroup) as any;

        expect(stockTransaction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStockTransaction should not enable id FormControl', () => {
        const formGroup = service.createStockTransactionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStockTransaction should disable id FormControl', () => {
        const formGroup = service.createStockTransactionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
