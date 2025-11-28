import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { observableToBeFn } from 'rxjs/internal/testing/TestScheduler';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class BrrowserviceService {

  constructor(private http: HttpClient) { }

  createBorrowNow(data: any): Observable<any>{
    return this.http.post(`${environment.apiUrl}${environment.endpoints.borrow.crateBorrow}`,data)
  }

  countBorrow(): Observable<any>{
    return this.http.get(`${environment.apiUrl}${environment.endpoints.borrow.countborrow}`)
  }

  getBorrowByid (userId: number):Observable<any>{
    return this.http.get(`${environment.apiUrl}${environment.endpoints.borrow.getBorrowById(userId)}`)
  }

  returnDevice (borrowId : number, payload : any):Observable<any>{
    return this.http.post(`${environment.apiUrl}${environment.endpoints.borrow.returnborrow(borrowId)}`,payload)
  }

  ExtendBorrow(reservationId : number , newReturnDue : string,extensionReason : string):Observable<any>{
    const body = {
      newReturnDue ,extensionReason
    }
    return this.http.put(`${environment.apiUrl}${environment.endpoints.borrow.ExtendBorrow(reservationId)}`,body)
  }

  countOverDue():Observable<any>{
    return this.http.get(`${environment.apiUrl}${environment.endpoints.borrow.countoverdue}`)
  }

  getAlmostDue():Observable<any>{
    return this.http.get(`${environment.apiUrl}${environment.endpoints.borrow.getalmostDue}`)
  }

  getAllBorrow():Observable<any>{
    return this.http.get(`${environment.apiUrl}${environment.endpoints.borrow.getallborrow}`)
  }

  countTotalBorrow():Observable<any>{
    return this.http.get(`${environment.apiUrl}${environment.endpoints.borrow.countTotalBorrow}`)
  }

  countReturnBorrow():Observable<any>{
    return this.http.get(`${environment.apiUrl}${environment.endpoints.borrow.countreturnborrow}`)
  }
}
