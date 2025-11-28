import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BorrowRequestComponentComponent } from './borrow-request-component.component';

describe('BorrowRequestComponentComponent', () => {
  let component: BorrowRequestComponentComponent;
  let fixture: ComponentFixture<BorrowRequestComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BorrowRequestComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BorrowRequestComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
