import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagereservationcomponentComponent } from './managereservationcomponent.component';

describe('ManagereservationcomponentComponent', () => {
  let component: ManagereservationcomponentComponent;
  let fixture: ComponentFixture<ManagereservationcomponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManagereservationcomponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManagereservationcomponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
