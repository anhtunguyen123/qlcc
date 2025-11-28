import { TestBed } from '@angular/core/testing';

import { BrrowserviceService } from './borrowservice.service';

describe('BrrowserviceService', () => {
  let service: BrrowserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BrrowserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
