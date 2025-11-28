import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageDeviceComponentComponent } from './manage-device-component.component';

describe('ManageDeviceComponentComponent', () => {
  let component: ManageDeviceComponentComponent;
  let fixture: ComponentFixture<ManageDeviceComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManageDeviceComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManageDeviceComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
