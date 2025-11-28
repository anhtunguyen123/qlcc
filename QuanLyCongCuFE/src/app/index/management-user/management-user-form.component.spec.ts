import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagementUserFormComponent } from './management-user-form.component';

describe('ManagementUserFormComponent', () => {
  let component: ManagementUserFormComponent;
  let fixture: ComponentFixture<ManagementUserFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManagementUserFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManagementUserFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
