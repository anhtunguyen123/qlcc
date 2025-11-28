import { HttpClientModule,HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserFormComponent } from './index/user-form/user-form.component';
import { ManagementUserFormComponent } from './index/management-user/management-user-form.component';
import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { HeaderComponentComponent } from './index/header-component/header-component.component';
import { SidebarComponentComponent } from './index/sidebar-component/sidebar-component.component';
import { BorrowRequestComponentComponent } from './index/Teacher/borrow-request-component/borrow-request-component.component';
import { ReturnDeviceComponentComponent } from './index/Teacher/return-device-component/return-device-component.component';
import { ReportBrokenComponentComponent } from './index/Teacher/report-broken-component/report-broken-component.component';
import { ManageDeviceComponentComponent } from './index/Admin/manage-device-component/manage-device-component.component';
import { ManageBorrowComponentComponent } from './index/Admin/manage-borrow-component/manage-borrow-component.component';
import { RouterModule } from '@angular/router';
import { HomeComponentComponent } from './index/home-component/home-component.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { MyreservationComponent } from './index/Teacher/my-reservation-component/myreservation.component';
import { ManagereservationcomponentComponent } from './index/Admin/manage-reservation-component/managereservationcomponent/managereservationcomponent.component';
import { NotFoundComponent } from './notfound/not-found/not-found.component';
import { DashboardComponent } from './index/Admin/dashboard/dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    UserFormComponent,
    ManagementUserFormComponent,
    HeaderComponentComponent,
    SidebarComponentComponent,
    BorrowRequestComponentComponent,
    ReturnDeviceComponentComponent,
    ReportBrokenComponentComponent,
    ManageDeviceComponentComponent,
    ManageBorrowComponentComponent,
    HomeComponentComponent,
    MyreservationComponent,
    ManagereservationcomponentComponent,
    NotFoundComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,  
    AppRoutingModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
      progressBar: true,
      progressAnimation: 'increasing',
      closeButton: true
    }),

  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
