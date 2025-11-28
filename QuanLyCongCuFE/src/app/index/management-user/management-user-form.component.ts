import { Component,OnInit } from '@angular/core';
import { UserServiceService } from '../../service/userservice/user-service.service';

@Component({
  selector: 'app-management-user-form',
  templateUrl: './management-user-form.component.html',
  styleUrl: './management-user-form.component.css'
})
export class ManagementUserFormComponent implements OnInit {

  listUser : any[] = []

  constructor(private userservice : UserServiceService){}

  ngOnInit(): void {
    this.loadUser();
    
  }

  loadUser(){
    this.userservice.getuser().subscribe({
      next : (res) =>{
        console.log(res)
        this.listUser = res
          console.log('Token nè:', localStorage.getItem('token'));
      } 
    })
  }

  deleteUser(userId : number){
    if(confirm('bạn có chắc chắn xóa user này không?')){
      this.userservice.deleteUser(userId).subscribe({
        next : (res) =>{
         alert('xóa thành công')
         this.loadUser()
        }
      })
    }
    
  }

}
