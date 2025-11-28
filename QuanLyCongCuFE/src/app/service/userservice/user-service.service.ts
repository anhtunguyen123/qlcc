import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../../environment/environment';


@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  constructor(private http : HttpClient) { }
  
  login(userlogin : any ): Observable<any>{
    return (this.http.post(`${environment.apiUrl}${environment.endpoints.user.login}`,userlogin));
  }

  register(user: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}${environment.endpoints.user.register}`, user);
  }

  getuser(): Observable<any> {
    return this.http.get(`${environment.apiUrl}${environment.endpoints.user.getuser}`);
  }

  deleteUser(userId: number): Observable<any> {
    return this.http.delete(`${environment.apiUrl}${environment.endpoints.user.delete(userId)}`);
  }
}
