import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { DeviceServiceService } from '../../../service/deviceservice/device-service.service';
import { SeachdeviceserviceService } from '../../../service/seachdeviceservice/seachdeviceservice.service';

@Component({
  selector: 'app-manage-device-component',
  templateUrl: './manage-device-component.component.html',
  styleUrl: './manage-device-component.component.css'
})
export class ManageDeviceComponentComponent {

  devices: any[] = [];
  rooms: any[] = []
  categories: any[] = []
  deviceForm!: FormGroup;
  showForm = false;
  isEdit = false;
  selectedDeviceId: number | null = 0;

  filter = {
    status: '',
    categoryId: '',
    roomId: ''
  };

  constructor(
    private deviceService: DeviceServiceService,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private seachservice: SeachdeviceserviceService
  ) { }

  ngOnInit() {
    this.loadDevices();
    this.initForm();
    this.loadRooms();
    this.loadCategories();

  }

  loadRooms() {
    this.seachservice.getRooms().subscribe((data) => this.rooms = data)
  }

  loadCategories() {
    this.seachservice.getCategories().subscribe((data) => this.categories = data)
  }

  loadDevices() {
    this.deviceService.getdevice().subscribe({
      next: (res) => {
        this.devices = res
        console.log("listdevice " + res);

      },
      error: (err) => {
        console.error(err)
      }
    });
  }

  initForm() {
    this.deviceForm = this.fb.group({
      deviceId: ['', Validators.required],
      deviceName: ['', Validators.required],
      categoryId: ['', Validators.required],
      roomId: ['', Validators.required],
      description: [''],
      isPortable: [false],
      status: ['AVAILABLE', Validators.required]
    });
  }

  openCreateForm() {
    this.showForm = true;
    this.isEdit = false;
    this.selectedDeviceId = null;
    this.deviceForm.reset({ status: 'AVAILABLE', isportable: false, categoryId: null, roomId: null });
  }

  openEditForm(device: any) {
    this.showForm = true;
    this.isEdit = true;
    this.selectedDeviceId = device.deviceId;
    this.deviceForm.patchValue(device);
  }

  submitForm() {
    if (this.deviceForm.invalid) {
      this.toastr.warning('Vui lòng điền đầy đủ thông tin');
      return;
    };

    if (this.isEdit && this.selectedDeviceId) {
      this.deviceService.update(this.selectedDeviceId, this.deviceForm.value).subscribe({
        next: () => {
          console.log("dữ liệu cập nhập thiết bị" + this.deviceForm);

          this.toastr.success('Cập nhật thiết bị thành công')
          this.showForm = false;
          this.loadDevices();
        }
      });
    } else {
      this.deviceService.createDevice(this.deviceForm.value).subscribe({
        next: () => {
          console.log("dữ liệu thêm thiết bị" + this.deviceForm);
          this.toastr.success('thêm thiết bị thành công')
          this.showForm = false;
          this.loadDevices();
        }
      });
    }
  }

  deleteDevice(deviceId: number) {
    if (!confirm('Bạn có chắc muốn xóa thiết bị này?')) return;

    this.deviceService.deleteDevice(deviceId).subscribe({
      next: () => {
        alert('Xóa thiết bị thành công');
        this.loadDevices();
      }
    });
  }

  applyFilter() {
    const categoryId = Number(this.filter.categoryId) ;

    console.log('Filter params:', {
      status: this.filter.status,
      categoryId: categoryId,
      roomId: this.filter.roomId
    });

    this.deviceService.fileterDevice(this.filter.status, categoryId, this.filter.roomId)
      .subscribe(res => {
        this.devices = res;
      });
  }


  resetFilters() {
    this.filter.status = '';
    this.filter.categoryId = '';
    this.filter.roomId = '';
    this.applyFilter();
  }
}
