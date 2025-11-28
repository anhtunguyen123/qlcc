import {NgModule } from '@angular/core';
import { RouterModule,Routes } from '@angular/router';
import { DashboardComponent } from './index/Admin/dashboard/dashboard.component';
import { ManageBorrowComponentComponent } from './index/Admin/manage-borrow-component/manage-borrow-component.component';
import { ManageDeviceComponentComponent } from './index/Admin/manage-device-component/manage-device-component.component';
import { ManagereservationcomponentComponent } from './index/Admin/manage-reservation-component/managereservationcomponent/managereservationcomponent.component';
import { HomeComponentComponent } from './index/home-component/home-component.component';
import { ManagementUserFormComponent } from './index/management-user/management-user-form.component';
import { BorrowRequestComponentComponent } from './index/Teacher/borrow-request-component/borrow-request-component.component';
import { MyreservationComponent } from './index/Teacher/my-reservation-component/myreservation.component';
import { ReturnDeviceComponentComponent } from './index/Teacher/return-device-component/return-device-component.component';
import { UserFormComponent } from './index/user-form/user-form.component';
import { NotFoundComponent } from './notfound/not-found/not-found.component';

const routes: Routes = [
  {
    path: '',component: HomeComponentComponent,
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'users', component: ManagementUserFormComponent },
      { path: 'devices', component: ManageDeviceComponentComponent },
      { path: 'borrowadmin', component: ManageBorrowComponentComponent },
      { path: 'borrow', component: BorrowRequestComponentComponent },
      { path: 'reservation', component: MyreservationComponent},
      {path: 'manage-reservation', component: ManagereservationcomponentComponent},
      {path: 'manage-borrow', component: ManageBorrowComponentComponent},
      {path: 'return-device', component : ReturnDeviceComponentComponent},
      {path: 'management-device', component : ManageDeviceComponentComponent},
      { path: 'dashboard', component: DashboardComponent },
    ]
  },
  { path: 'login', component: UserFormComponent },
  { path: '**',component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
