import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageBorrowComponentComponent } from './manage-borrow-component.component';

describe('ManageBorrowComponentComponent', () => {
  let component: ManageBorrowComponentComponent;
  let fixture: ComponentFixture<ManageBorrowComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManageBorrowComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManageBorrowComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
