import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class MaintenanceServiceService {

  constructor(private http: HttpClient) { }

  CountMaintenance(): Observable<any>{
    return this.http.get(`${environment.apiUrl}${environment.endpoints.maintenance.countmaintenance}`)
  }
}
