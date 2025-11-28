import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class DeviceServiceService {

  constructor(private http : HttpClient) { }

  getdevice(): Observable<any> {
    return this.http.get(`${environment.apiUrl}${environment.endpoints.device.getAll}`);
  }

  deleteDevice(deviceId: number): Observable<any> {
    return this.http.delete(`${environment.apiUrl}${environment.endpoints.device.delete(deviceId)}`);
  }

  countDevice(): Observable<any> {
    return this.http.get(`${environment.apiUrl}${environment.endpoints.device.count}`);
  }

  createDevice(device: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}${environment.endpoints.device.create}`, device);
  }

  searchAvailableDevices(req: any): Observable<any[]> {
    return this.http.post<any[]>(`${environment.apiUrl}${environment.endpoints.device.searchAvailable}`, req);
  }

  getAvailableDevice(): Observable<any>{
    return this.http.get(`${environment.apiUrl}${environment.endpoints.device.getAvailableDevice}`)
  }

  update(deviceId : number,data: any):Observable<any>{
    return this.http.put(`${environment.apiUrl}${environment.endpoints.device.updateDevice(deviceId)}`,data)
  }
  
  fileterDevice (status: String , categoryId : number|null, roomId :string):Observable<any>{
      let params: any = {};
      if (status) params.status = status;
      if (categoryId) params.categoryId = categoryId;
      if (roomId) params.roomId = roomId;
      return this.http.get(`${environment.apiUrl}${environment.endpoints.device.fileterDevice}`,{params})
    }
}
