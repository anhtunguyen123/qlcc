import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ReservationserviceService } from '../../../../service/reservationservice/reservationservice.service';

@Component({
  selector: 'app-managereservationcomponent',
  templateUrl: './managereservationcomponent.component.html',
  styleUrl: './managereservationcomponent.component.css'
})
export class ManagereservationcomponentComponent implements OnInit {
  listReservation : any = []

  constructor(private reservationService : ReservationserviceService, private toastr : ToastrService){}

  ngOnInit(): void {
    this.getReservationbyStatus()
  }
  
  getReservationbyStatus(){
    this.reservationService.getstatuspending().subscribe(
      (res) => {
        console.log('yêu cầu mượn'+res);
          this.listReservation = res
      }
    )
  }

  updateStatus(id : number, status : string){
    this.reservationService.updateStutus(id, status).subscribe(
      (res)=>{
        console.log(id , status);
        this.toastr.success(`Đã ${status === 'APPROVED' ? 'chấp nhận' : 'huỷ'} yêu cầu thành công!`);
        this.getReservationbyStatus();
      },(err) => {
        const errorMsg = err.error?.message || 'thời gian quá hạn!';
        this.toastr.error(errorMsg);
      }
    )
  }
}
