import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportBrokenComponentComponent } from './report-broken-component.component';

describe('ReportBrokenComponentComponent', () => {
  let component: ReportBrokenComponentComponent;
  let fixture: ComponentFixture<ReportBrokenComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReportBrokenComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportBrokenComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
