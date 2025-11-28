import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { BrrowserviceService } from '../../../service/borrowservice/borrowservice.service';
import { ReservationserviceService } from '../../../service/reservationservice/reservationservice.service';

@Component({
  selector: 'app-myreservation',
  templateUrl: './myreservation.component.html',
  styleUrl: './myreservation.component.css'
})
export class MyreservationComponent implements OnInit {

  userId!: number
  reservations: any[] = [];
  ShowExtendForm = false;
  selectedReservation: any = null;
  newEndTime: string = '';
  extensionReason: string = '';

  showEditForm = false;
  editStartTime: string = '';
  editEndTime: string = '';
  editPurpose: string = '';
  editNotes: string = '';
  minEditDate: string = '';


  filter = {
    status: '',
    startDate: ''
  };

  constructor(private reservationservice: ReservationserviceService, private toastr: ToastrService, private router: Router, private borrowService: BrrowserviceService) { }

  ngOnInit(): void {
    this.getreservation();
  }

  getreservation() {
    this.userId = Number(localStorage.getItem('userId'))

    this.reservationservice.getreservation(this.userId).subscribe(
      {
        next: (res) => {
          this.reservations = res;
        },
        error: (error) => {
          this.toastr.error('Lỗi tải danh sách mượn');
        }
      }
    )
  }

  cancelReservation(id: number) {
    this.reservationservice.deleteRs(id).subscribe({
      next: (res) => {
        if (!confirm('Bạn có chắc muốn xóa yêu cầu này?')) return;
        this.toastr.success("Hủy thành công");
        this.getreservation()
      },
      error: (err) => {
        console.log(err);
        this.toastr.error("Hủy thất bại");
      }
    });
  }

  applyFilter() {
    this.reservationservice.filterRs(this.filter.status, this.filter.startDate)
      .subscribe(res => {
        this.reservations = res;
      });
  }

  resetFilters() {
    this.filter.status = '';
    this.filter.startDate = '';
    this.applyFilter();
  }

  createNewReservation() {
    this.router.navigate(['/borrow'])
  }

  closeExtendForm() {
    this.ShowExtendForm = false;
    this.selectedReservation = null;
  }

  submitExtension() {
    if (!this.newEndTime) {
      this.toastr.warning('Vui lòng chọn thời gian trả mới');
      return;
    }

    if (!this.extensionReason) {
      this.toastr.warning('Vui lòng nhập lý do gia hạn');
      return;
    }
    const newEnd = new Date(this.newEndTime);
    const currentEnd = new Date(this.selectedReservation.endTime);

     if (newEnd <= currentEnd) {
        this.toastr.warning('Thời gian gia hạn phải sau thời gian trả hiện tại');
        return;
    }

    this.borrowService.ExtendBorrow(this.selectedReservation.reservationId, this.newEndTime, this.extensionReason).subscribe({
      next: (res) => {
        this.toastr.success(" gia hạn thành công")
        this.closeExtendForm();
        this.getreservation(); 
      },
      error: (err) => {
        console.error("Lỗi gia hạn:", err);
        this.toastr.error(err.error?.message || "Bị trùng lich");
      }

    })

  }

  openExtendModal(borrow: any) {
    this.selectedReservation = borrow;
    this.newEndTime = '';
    this.extensionReason = '';

    this.ShowExtendForm = true;
  }

  openEditModal(reservation: any) {
    this.selectedReservation = reservation;
    this.editStartTime = this.selectedReservation.startTime;
    this.editEndTime = this.selectedReservation.endTime;
    this.editPurpose = reservation.purpose;
    this.editNotes = reservation.notes || '';
    
    this.showEditForm = true;
  }

  closeEditForm() {
    this.showEditForm = false;
    this.selectedReservation = null;
    this.resetEditForm();
  }

  resetEditForm() {
    this.editStartTime = '';
    this.editEndTime = '';
    this.editPurpose = '';
    this.editNotes = '';
  }

  isEditFormValid(): boolean {
    if (!this.editStartTime || !this.editEndTime || !this.editPurpose) {
      return false;
    }

    const startTime = new Date(this.editStartTime);
    const endTime = new Date(this.editEndTime);
    const now = new Date();

    if (endTime <= startTime) {
      return false;
    }

    return true;
  }

  submitEdit() {
    if (!this.isEditFormValid()) {
      this.toastr.warning('Vui lòng kiểm tra lại thông tin');
      return;
    }

    const updateData = {
      startTime: this.editStartTime,
      endTime: this.editEndTime,
      purpose: this.editPurpose,
    };
    console.log(updateData);
    
    this.reservationservice.updateReservation(this.selectedReservation.reservationId, updateData)
      .subscribe({
        next: (res) => {
        
          this.toastr.success('Cập nhật thành công');
          this.closeEditForm();
          this.getreservation();
        },
        error: (err) => {
          this.toastr.error(err.error?.message || 'Thiết bị bị trùng lịch');
        }
      });
  }
}