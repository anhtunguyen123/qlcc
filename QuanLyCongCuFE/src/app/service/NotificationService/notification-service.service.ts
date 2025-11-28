import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { BehaviorSubject } from 'rxjs';

export interface NotificationPayload {
  notificationId: number;
  title: string;
  message: string;
  isRead: boolean;
  relatedType?: string;
  relatedId?: number;
  createdAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationServiceService {

  private stompClient: Client | null = null;
  private baseUrl = 'http://localhost:8080/api/notifications';

  private notifications$ = new BehaviorSubject<NotificationPayload[]>([]);
  public notificationsObservable = this.notifications$.asObservable();
  
  constructor(private toastr: ToastrService, private http: HttpClient) {}

  connect(userId: number) {
    const socket = new SockJS('http://localhost:8080/ws');

    this.stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("WebSocket connected for user", userId);

        this.stompClient?.subscribe(`/user/${userId}/notification`, (message: any) => {
          const payload: NotificationPayload = JSON.parse(message.body);
          const current = this.notifications$.value;
          this.notifications$.next([payload, ...current]);

          this.toastr.info(payload.message, payload.title);
        });

        this.loadNotifications(userId);
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ', frame.headers['message'], frame.body);
      },
      onWebSocketError: (evt) => {
        console.error('WebSocket error: ', evt);
      }
    });

    this.stompClient.activate();
  }

  loadNotifications(userId: number) {
    this.http.get<NotificationPayload[]>(`${this.baseUrl}/${userId}`)
      .subscribe(list => {
        this.notifications$.next(list);
      });
  }

  getUnreadCount(userId: number) {
    return this.http.get<{unread:number}>(`${this.baseUrl}/${userId}/unreadcount`);
  }

  markAsRead(notificationId: number) {
    return this.http.put(`${this.baseUrl}/mark-read/${notificationId}`, {});
  }

  getNotificationsSnapshot(): NotificationPayload[] {
    return this.notifications$.value;
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
      console.log("WebSocket disconnected");
    }
  }
}
