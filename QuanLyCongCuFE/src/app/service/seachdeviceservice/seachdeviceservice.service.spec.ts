import { TestBed } from '@angular/core/testing';

import { SeachdeviceserviceService } from './seachdeviceservice.service';

describe('SeachdeviceserviceService', () => {
  let service: SeachdeviceserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SeachdeviceserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
