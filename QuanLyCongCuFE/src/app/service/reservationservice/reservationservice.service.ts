import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class ReservationserviceService {

  constructor(private http: HttpClient) { }

  reservationDevice(data: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}${environment.endpoints.reservation.create}`, data);
  }

  getreservation(UserId: number): Observable<any> {
    return this.http.get(`${environment.apiUrl}${environment.endpoints.reservation.getByUser(UserId)}`)
  }

  getstatuspending(): Observable<any> {
    return this.http.get(`${environment.apiUrl}${environment.endpoints.reservation.getstatuspending}`)
  }

  updateStutus(id: number, status: string): Observable<any> {
    return this.http.put(`${environment.apiUrl}${environment.endpoints.reservation.updateStatus(id)}`, { status })
  }

  deleteRs(id: number): Observable<any> {
    return this.http.delete(`${environment.apiUrl}${environment.endpoints.reservation.deleteRs(id)}`)
  }

  filterRs(status: String, startTime: String): Observable<any> {
    let params: any = {};
    if (status) params.status = status;
    if (startTime) params.startTime = startTime;
    return this.http.get(`${environment.apiUrl}${environment.endpoints.reservation.filterReservation}`,{params})
  }

  updateReservation(reservationId: number,data: any): Observable<any> {
    return this.http.put(`${environment.apiUrl}${environment.endpoints.reservation.updateReservation(reservationId)}`,data)
  }
}
