import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {
  private loggedIn = new BehaviorSubject<boolean>(false);
  public isLoggedIn$ = this.loggedIn.asObservable();

  constructor() {
    const token = localStorage.getItem('token');
    if (token) this.loggedIn.next(true);
  }

  setLoggedIn(status: boolean) {
    this.loggedIn.next(status);
  }

  logout() {
    localStorage.clear()
    this.loggedIn.next(false);
  }
}
