import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthServiceService } from '../../service/AuthService/auth-service.service';
import { NotificationPayload, NotificationServiceService } from '../../service/NotificationService/notification-service.service';

@Component({
  selector: 'app-header-component',
  templateUrl: './header-component.component.html',
  styleUrl: './header-component.component.css'
})
export class HeaderComponentComponent implements OnInit{
  isLoggedIn: boolean = false;
  isUserMenuOpen = false
  isProfileOpen = false;
  isNotificationOpen = false;

  
  fullname : string | null = "";
  role : string | null = ""
  userId : number| null = 0
  Email : string | null = ""
  notifications: NotificationPayload[] = [];
  unreadCount: number = 0;

  constructor(private router : Router, private authService : AuthServiceService,
    private notificationService: NotificationServiceService){}

  ngOnInit(): void {
    const userData = JSON.parse(localStorage.getItem('currentUser') || 'null');
    const email = userData?.email;
    console.log("thông tin user" + email);
    
    this.fullname  = localStorage.getItem('fullname')
    this.role = localStorage.getItem('role')
    this.userId = Number(localStorage.getItem('userId'))
    this.Email = userData.email
    this.notificationService.loadNotifications(this.userId);
    if (this.userId) {
      this.notificationService.connect(this.userId);
      
      this.notificationService.notificationsObservable.subscribe(res => {
        this.notifications = res;
        this.unreadCount = res.filter(n => !n.isRead).length;
      });
    
      
    }

    this.authService.isLoggedIn$.subscribe(status => {
      this.isLoggedIn = status;
    }); 
  }

  toggleUserMenu() {
    this.isUserMenuOpen = !this.isUserMenuOpen
  }

  logout(){
    this.notificationService.disconnect();
    this.authService.logout();
    this.router.navigate(['/login']);
    
  }
  
  login(){
    this.router.navigate(['/login']);
  }

  openProfile(event: Event) {
    event.preventDefault()
    this.isProfileOpen = true
    document.body.style.overflow = "hidden"
  }

  closeProfile() {
    this.isProfileOpen = false
    document.body.style.overflow = "auto"
  }

  toggleNotificationPanel() {
    this.isNotificationOpen = !this.isNotificationOpen;
  }

  openNotification(n: NotificationPayload) {
    if (!n.isRead) {
      this.notificationService.markAsRead(n.notificationId).subscribe(() => {
        n.isRead = true;
        this.unreadCount = this.notifications.filter(x => !x.isRead).length;
      });
    }
    console.log('Open notification:', n.relatedType, n.relatedId);
  }

}
