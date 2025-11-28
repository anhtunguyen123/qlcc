import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReturnDeviceComponentComponent } from './return-device-component.component';

describe('ReturnDeviceComponentComponent', () => {
  let component: ReturnDeviceComponentComponent;
  let fixture: ComponentFixture<ReturnDeviceComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReturnDeviceComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReturnDeviceComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
