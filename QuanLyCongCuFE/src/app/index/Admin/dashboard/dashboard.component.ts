import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { BrrowserviceService } from '../../../service/borrowservice/borrowservice.service';
import { DeviceServiceService } from '../../../service/deviceservice/device-service.service';
import { MaintenanceServiceService } from '../../../service/maintenanceService/maintenance-service.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{

  countdevice : number = 0
  countborrow : number = 0
  countmaintenance : number = 0
  countborrowoverdue : Number = 0
  ListBorrowAlmostDue : any[] = []

  constructor(private deviceService : DeviceServiceService,
     private toastr : ToastrService,
     private borrowService : BrrowserviceService,
     private maintenanceService : MaintenanceServiceService){}

  ngOnInit(): void {
    this.countDevice()
    this.countBorrow()
    this.countMaintenance()
    this.countOverDue()
    this.getListBorrowAlmostDue()
  }

  countDevice(){
    this.deviceService.countDevice().subscribe({
      next : (res) =>{
        this.countdevice = res
        console.log(res);
      }
     })
  }

  countBorrow(){
    this.borrowService.countBorrow().subscribe({
     next: (res)=>{
        this.countborrow = res
      }
    })
  }

  countMaintenance(){
    this.maintenanceService.CountMaintenance().subscribe({
     next: (res)=>{
        this.countMaintenance = res
      }
    })
  }

  countOverDue(){
    this.borrowService.countOverDue().subscribe({
     next: (res)=>{
      console.log(res);
      
        this.countborrowoverdue = res
      }
    })
  }

  getListBorrowAlmostDue(){
    this.borrowService.getAlmostDue().subscribe({
      next : (res) =>{
        this.ListBorrowAlmostDue = res
        console.log("đây là thiết bị gần trả ",res);
        
      }
    })
  }
}

