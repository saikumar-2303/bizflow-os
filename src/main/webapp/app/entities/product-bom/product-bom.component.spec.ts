import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductBomComponent } from './product-bom.component';

describe('ProductBomComponent', () => {
  let component: ProductBomComponent;
  let fixture: ComponentFixture<ProductBomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductBomComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductBomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
