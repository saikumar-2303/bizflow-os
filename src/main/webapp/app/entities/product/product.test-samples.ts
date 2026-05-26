import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 11737,
  name: 'hm sleepily',
  buyPrice: 17510.56,
  sellPrice: 1843.12,
  stockQuantity: 24770,
  active: true,
};

export const sampleWithPartialData: IProduct = {
  id: 31533,
  name: 'almost tempting fervently',
  description: 'ick for',
  buyPrice: 26.65,
  sellPrice: 8672.51,
  stockQuantity: 4492,
  active: true,
};

export const sampleWithFullData: IProduct = {
  id: 4403,
  name: 'cafe',
  category: 'along amongst',
  description: 'hm ugh',
  buyPrice: 2989.07,
  sellPrice: 1604.77,
  stockQuantity: 24746,
  lowStockAlert: 6387,
  barcode: 'bah though pear',
  active: true,
};

export const sampleWithNewData: NewProduct = {
  name: 'anti inject why',
  buyPrice: 26562.23,
  sellPrice: 4718.75,
  stockQuantity: 6371,
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
