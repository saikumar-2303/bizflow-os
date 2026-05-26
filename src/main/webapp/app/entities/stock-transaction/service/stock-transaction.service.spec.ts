import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStockTransaction } from '../stock-transaction.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../stock-transaction.test-samples';

import { RestStockTransaction, StockTransactionService } from './stock-transaction.service';

const requireRestSample: RestStockTransaction = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('StockTransaction Service', () => {
  let service: StockTransactionService;
  let httpMock: HttpTestingController;
  let expectedResult: IStockTransaction | IStockTransaction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StockTransactionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a StockTransaction', () => {
      const stockTransaction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stockTransaction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StockTransaction', () => {
      const stockTransaction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stockTransaction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StockTransaction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StockTransaction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StockTransaction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStockTransactionToCollectionIfMissing', () => {
      it('should add a StockTransaction to an empty array', () => {
        const stockTransaction: IStockTransaction = sampleWithRequiredData;
        expectedResult = service.addStockTransactionToCollectionIfMissing([], stockTransaction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockTransaction);
      });

      it('should not add a StockTransaction to an array that contains it', () => {
        const stockTransaction: IStockTransaction = sampleWithRequiredData;
        const stockTransactionCollection: IStockTransaction[] = [
          {
            ...stockTransaction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStockTransactionToCollectionIfMissing(stockTransactionCollection, stockTransaction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StockTransaction to an array that doesn't contain it", () => {
        const stockTransaction: IStockTransaction = sampleWithRequiredData;
        const stockTransactionCollection: IStockTransaction[] = [sampleWithPartialData];
        expectedResult = service.addStockTransactionToCollectionIfMissing(stockTransactionCollection, stockTransaction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockTransaction);
      });

      it('should add only unique StockTransaction to an array', () => {
        const stockTransactionArray: IStockTransaction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stockTransactionCollection: IStockTransaction[] = [sampleWithRequiredData];
        expectedResult = service.addStockTransactionToCollectionIfMissing(stockTransactionCollection, ...stockTransactionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stockTransaction: IStockTransaction = sampleWithRequiredData;
        const stockTransaction2: IStockTransaction = sampleWithPartialData;
        expectedResult = service.addStockTransactionToCollectionIfMissing([], stockTransaction, stockTransaction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockTransaction);
        expect(expectedResult).toContain(stockTransaction2);
      });

      it('should accept null and undefined values', () => {
        const stockTransaction: IStockTransaction = sampleWithRequiredData;
        expectedResult = service.addStockTransactionToCollectionIfMissing([], null, stockTransaction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockTransaction);
      });

      it('should return initial array if no StockTransaction is added', () => {
        const stockTransactionCollection: IStockTransaction[] = [sampleWithRequiredData];
        expectedResult = service.addStockTransactionToCollectionIfMissing(stockTransactionCollection, undefined, null);
        expect(expectedResult).toEqual(stockTransactionCollection);
      });
    });

    describe('compareStockTransaction', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStockTransaction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10234 };
        const entity2 = null;

        const compareResult1 = service.compareStockTransaction(entity1, entity2);
        const compareResult2 = service.compareStockTransaction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10234 };
        const entity2 = { id: 29569 };

        const compareResult1 = service.compareStockTransaction(entity1, entity2);
        const compareResult2 = service.compareStockTransaction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 10234 };
        const entity2 = { id: 10234 };

        const compareResult1 = service.compareStockTransaction(entity1, entity2);
        const compareResult2 = service.compareStockTransaction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
