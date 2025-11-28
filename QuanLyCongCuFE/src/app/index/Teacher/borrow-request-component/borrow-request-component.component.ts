import { Component, HostListener } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ReservationserviceService } from '../../../service/reservationservice/reservationservice.service';
import { SeachdeviceserviceService } from '../../../service/seachdeviceservice/seachdeviceservice.service';

@Component({
  selector: 'app-borrow-request-component',
  templateUrl: './borrow-request-component.component.html',
  styleUrl: './borrow-request-component.component.css'
})
export class BorrowRequestComponentComponent {
  searchForm!: FormGroup
  rooms: any[] = []
  categories: any[] = []
  results: any[] = []
  groupedRooms: any[] = []
  dropdownOpen = false;
  expandedBuilding: string | null = null
  openedBuilding: string | null = null;
  selectedRoomName: string = '';
  selectedRoomId: number | null = null;
  showReservationForm = false
  selectDevice : any = null
  purpose : string | null = ""
  startTime!: string
  endtime!: string
  showBorrowForm = false
  selectedDeviceBorrow: any = null
  borrowPurpose : string|null = ""
  startTimebrrow! : string
  endtimebrrow! : string

  constructor(private fb: FormBuilder, private seachservice: SeachdeviceserviceService, private reservationservice : ReservationserviceService,  private toastr : ToastrService) {}

  ngOnInit(): void {
    this.searchForm = this.fb.group({
      startTime: ['', Validators.required],
      lessonCount: [1, Validators.required],
      roomId: ['', Validators.required],
      categoryId: ['', Validators.required],
    });

    this.loadRooms()
    this.loadCategories()
    if (!this.dropdownOpen) {
      this.expandedBuilding = null
    }
  }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!target.closest('.nested-dropdown')) {
      this.dropdownOpen = false;
    }
  }

  closeDropdown() {
    this.dropdownOpen = false;
  }

  selectRoom(room: any) {
    console.log('Selected room:', room);
    this.selectedRoomId = room.roomId;
    this.selectedRoomName = room.roomName;
    this.dropdownOpen = false;
    this.expandedBuilding = null

    this.searchForm.patchValue({
      roomId: room.roomId
    });
  }

  toggleBuilding(building: string, event: Event) {
    event.stopPropagation();
    
    this.expandedBuilding = this.expandedBuilding === building ? null : building
  }

  getRoomsByBuilding(building: string): any[] {
    const group = this.groupedRooms.find(g => g.building === building);
    return group ? group.rooms : [];
  }
  
  loadRooms(){
    this.seachservice.getRooms().subscribe((data) =>{
      console.log(data)
      this.rooms = data
      const map = new Map<string, any[]>();
      data.forEach(room => {
        if (!room.building) return;
  
        if (!map.has(room.building)) {
          map.set(room.building, []);
        }
        map.get(room.building)!.push(room);
      });
      this.groupedRooms = Array.from(map, ([building, rooms]) => ({
        building,
        rooms
      }));
      console.log('Grouped rooms:', this.groupedRooms);
    })
  }

  loadCategories() {
    this.seachservice.getCategories().subscribe((data) => this.categories = data)
  }

  searchDevices() {
    if(this.searchForm.invalid){
      this.toastr.warning('Vui lòng nhập đầy đủ thông tin!');
      return;
    };
    this.seachservice.searchAvailableDevices(this.searchForm.value)
      .subscribe((res) => {
        this.results = res;
      });
  }

  OpenFormReservation(device : any){
    this.showReservationForm = true
    this.selectDevice = device
  }

  closeReservationForm(){
    this.showReservationForm = false
  }

  OpenFormBorrow(device: any) {
    this.showBorrowForm = true;
    this.startTimebrrow = this.searchForm.get('startTime')?.value;
    this.endtimebrrow = this.searchForm.get('lessonCount')?.value;
    this.selectedDeviceBorrow = device
  }

  closeBorrowForm(){
    this.showBorrowForm = false;
  }

  reservationDevice(){
    const request = {
      deviceId: this.selectDevice.deviceId,
      userId : localStorage.getItem('userId'),
      startTime: this.startTime,
      lessonCount: this.endtime,
      purpose: this.purpose
    };
    
    this.reservationservice.reservationDevice(request).subscribe(
      (res: any) => {    
          this.toastr.success("Đặt chỗ thành công")
          this.closeReservationForm()
        },
      (err) => {
        if (err.status === 400) {
          this.toastr.error("thời gian không hợp lệ!");
        }
        else if (err.status === 409) {
          this.toastr.error("Thiết bị đã có người đặt trong thời gian này!");
        }
        else {
          this.toastr.error("Lỗi hệ thống, vui lòng thử lại");
        }
      }
    );
  }

  borrowDevice(){
    const request = {
      deviceId: this.selectedDeviceBorrow.deviceId,
      userId : localStorage.getItem('userId'),
      startTime: this.startTimebrrow,
      lessonCount: this.endtimebrrow,
      purpose: this.borrowPurpose
    };

    this.reservationservice.reservationDevice(request).subscribe(
    (res) =>{
      this.toastr.success("Đặt mượn thiết bị thành công");
      this.closeBorrowForm()
    },
    (err) => {
      if (err.status === 400) {
        this.toastr.error("thời gian không hợp lệ!");
      }
      else if (err.status === 409) {
        this.toastr.error("Thiết bị đã có người đặt trong thời gian này!");
      }
      else {
        this.toastr.error("Lỗi hệ thống, vui lòng thử lại");
      }
    }
  )
}
}
