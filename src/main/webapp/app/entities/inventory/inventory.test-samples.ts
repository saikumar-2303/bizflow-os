import { IInventory, NewInventory } from './inventory.model';

export const sampleWithRequiredData: IInventory = {
  id: 19402,
  inventory_id: 'dally tomography whoa',
};

export const sampleWithPartialData: IInventory = {
  id: 31019,
  inventory_id: 'off repurpose filthy',
  remarks: 'equatorial on',
};

export const sampleWithFullData: IInventory = {
  id: 6719,
  inventory_id: 'cleverly poppy',
  remarks: 'motor seriously',
  location: 'entwine apropos',
  description: 'deceivingly gnaw',
};

export const sampleWithNewData: NewInventory = {
  inventory_id: 'whether league after',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
