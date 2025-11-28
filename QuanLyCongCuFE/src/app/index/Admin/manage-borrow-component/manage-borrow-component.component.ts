import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { BrrowserviceService } from '../../../service/borrowservice/borrowservice.service';
import { DeviceServiceService } from '../../../service/deviceservice/device-service.service';
import { ReservationserviceService } from '../../../service/reservationservice/reservationservice.service';
import { UserServiceService } from '../../../service/userservice/user-service.service';

@Component({
  selector: 'app-manage-borrow-component',
  templateUrl: './manage-borrow-component.component.html',
  styleUrl: './manage-borrow-component.component.css'
})
export class ManageBorrowComponentComponent {
  ListBorrow : any[] = []
  totalBorrows: number = 0;
  borrowedCount: number = 0;
  returnedCount: number = 0;
  overdueCount: number = 0;

  constructor(
    private fb: FormBuilder,
    private borrowService: BrrowserviceService,
    private toastr: ToastrService,
  ) {}

  ngOnInit(): void {
    this.getAllBorrow()
    this.countBorrow()
    this.countOverDue()
    this.countTotalBorrow()
    this.countReturnBorrow()
  }

  filter = {
    status: '',
    borrowDate: ''
  };

  isOverdue(borrow: any): boolean {
    if (borrow.status === 'RETURNED') return false;
    
    const now = new Date();
    const returnDue = new Date(borrow.returnDue);
    return returnDue < now;
  }

  getRowClass(borrow: any): string {
    if (borrow.status === 'RETURNED') return 'returned';
    if (this.isOverdue(borrow)) return 'overdue';
    if (borrow.status === 'BORROWED') return 'borrowed';
    return '';
  }

  getAllBorrow(){
    this.borrowService.getAllBorrow().subscribe({
      next : (res) =>{
        this.ListBorrow = res
        console.log(res);
        
      }
    })
  }

  countBorrow(){
    this.borrowService.countBorrow().subscribe({
     next: (res)=>{
        this.borrowedCount = res
      }
    })
  }

  countTotalBorrow(){
    this.borrowService.countTotalBorrow().subscribe({
     next: (res)=>{
        this.totalBorrows = res
      }
    })
  }

  countOverDue(){
    this.borrowService.countOverDue().subscribe({
     next: (res)=>{
      console.log(res);
      
        this.overdueCount = res
      }
    })
  }

  countReturnBorrow(){
    this.borrowService.countReturnBorrow().subscribe({
     next: (res)=>{
        this.returnedCount = res
      }
    })
  }

 
}

