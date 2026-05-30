import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 11737,
  sku: 'hm sleepily',
  barcode: 'yippee whoever',
  name: 'mortally loftily',
};

export const sampleWithPartialData: IProduct = {
  id: 5830,
  sku: 'tempting',
  barcode: 'majestically ick',
  name: 'abnegate',
  shape: 'between',
  lowStockAlert: 20654,
  remarks: 'taro',
};

export const sampleWithFullData: IProduct = {
  id: 4403,
  sku: 'cafe',
  barcode: 'along amongst',
  name: 'hm ugh',
  category: 'fatally',
  shape: 'headline incidentally hence',
  retailPack: 207,
  wholesalePack: 31489,
  description: 'drowse under ferociously',
  stockQuantity: 28862,
  lowStockAlert: 5948,
  remarks: 'lest leap solemnly',
  location: 'usually gosh',
  message: 'cannon impractical',
  value: 'rotten psst',
};

export const sampleWithNewData: NewProduct = {
  sku: 'anti inject why',
  barcode: 'anenst narrow apropos',
  name: 'pace',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
