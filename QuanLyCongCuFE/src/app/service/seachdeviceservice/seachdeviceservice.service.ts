import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class SeachdeviceserviceService {

  constructor(private http: HttpClient) { }
  
  searchAvailableDevices(data: any):Observable<any> {
    return this.http.post(`${environment.apiUrl}${environment.endpoints.device.searchAvailable}`, data);
  }
  
  getRooms() {
    return this.http.get<any[]>(`${environment.apiUrl}${environment.endpoints.classroom.getAll}`);
  }
  
  getCategories() {
    return this.http.get<any[]>(`${environment.apiUrl}${environment.endpoints.category.getAll}`);
  }
}
