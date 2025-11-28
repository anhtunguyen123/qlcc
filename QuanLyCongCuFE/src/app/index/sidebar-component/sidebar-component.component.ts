import { Component, HostListener, OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar-component',
  templateUrl: './sidebar-component.component.html',
  styleUrls: ['./sidebar-component.component.css']
})
export class SidebarComponentComponent implements OnInit {
  isSidebarCollapsed = false;
  isSidebarOpen = false;
  isMobile = false;
  role :string =  ""

  constructor() {}

  ngOnInit() {
    this.checkMobileView();
    this.role = localStorage.getItem("role") || '';
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.checkMobileView();
  }

  checkMobileView() {
    this.isMobile = window.innerWidth < 768;
    if (!this.isMobile) {
      this.isSidebarOpen = false;
    }
  }

  toggleSidebar() {
    if (!this.isMobile) {
      this.isSidebarCollapsed = !this.isSidebarCollapsed;
    }
  }

  toggleMobileSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  closeMobileSidebar() {
    this.isSidebarOpen = false;
  }

  isAdmin(): boolean {
    return this.role === 'ADMIN';
  }
  
  isTeacher(): boolean {
    return this.role === 'TEACHER';
  }
  
}