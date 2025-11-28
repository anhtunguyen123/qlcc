import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { BrrowserviceService } from '../../../service/borrowservice/borrowservice.service';

@Component({
  selector: 'app-return-device-component',
  templateUrl: './return-device-component.component.html',
  styleUrl: './return-device-component.component.css'
})
export class ReturnDeviceComponentComponent implements OnInit {
  role: string | null = ""
  userId: number = 0
  listBorrow: any = []
  selectedBorrow: any = null
  searched: boolean = false; 

  returnForm = {
    returnDate: '',
    condition: '',  
    notes: '',
    receivedBy: ''
  };

  ngOnInit(): void {
    this.role = localStorage.getItem('role')
  }

  constructor(private borrowService: BrrowserviceService, private toastr: ToastrService,) { }

  searchMyBorrows() {
    this.userId = Number(localStorage.getItem('userId'))
    this.searched = true; 
    this.borrowService.getBorrowByid(this.userId).subscribe({
      next: (res) => {
        this.listBorrow = res
        console.log(res);
        
      },
      error: (err) => {
        this.toastr.error('Không tìm thấy thiết bị mượn!')
      }
    })
  }

  returnDevice() {
    if (!this.selectedBorrow) {
      this.toastr.error('Vui lòng chọn thiết bị để trả');
      return;
    }

    const payload = {
      returnDate: this.returnForm.returnDate,
      condition: this.returnForm.condition,
      notes: this.returnForm.notes,
      receivedBy: this.returnForm.receivedBy
    };

    if(payload.returnDate < this.selectedBorrow.borrowDate){
      this.toastr.error('Thời gian trả không hợp lệ');
      return;
    }
  
    this.borrowService.returnDevice(this.selectedBorrow.borrowId,payload).subscribe({
      next: (res) => {
        this.toastr.success('Trả thiết bị thành công!');
        this.selectedBorrow = null;
        this.resetForm()
        this.searchMyBorrows();
      },
      error: (err) => {
        this.toastr.error(' lỗi khi trả thiết bị!');
      }
    })
  }
  selectBorrow(borrow: any) {
    if (this.selectedBorrow?.borrowId === borrow.borrowId) {
      this.selectedBorrow = null;
    } else {
      this.selectedBorrow = borrow;
    }
  }

  resetForm(){
    this.returnForm = {
      returnDate: '',
      condition: '',
      notes: '',
      receivedBy: ''
  };
    this.selectedBorrow = null;
  }

}
