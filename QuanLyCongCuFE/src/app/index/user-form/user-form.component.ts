import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthServiceService } from '../../service/AuthService/auth-service.service';
import { UserServiceService } from '../../service/userservice/user-service.service';

  @Component({
    selector: 'app-user-form',
    templateUrl: './user-form.component.html',
    styleUrls: ['./user-form.component.css'] 
  })
  export class UserFormComponent {

    Showlogin = true ;
    Email : string = "";
    PassWord : string = "";
    FullName : string = "";
    ConfirmPass : string = "";
    emailExists = false;
    isloading = false;

    constructor(private userservice : UserServiceService,
      private router : Router ,
      private toastr : ToastrService,
      private authService : AuthServiceService){}

    showFormLogin(){
      this.Showlogin = true;
    }

    showFormRegister(){
      this.Showlogin = false;
    }

    login(){
      const userlogin = {
        email: this.Email,
        passwordHash: this.PassWord
      };

      if (this.isloading) return;
      this.isloading = true;

      this.userservice.login(userlogin).subscribe({
        next : (res) =>{
          this.isloading = false
          this.toastr.success("Đăng nhập thành công")
          this.authService.setLoggedIn(true); 
          console.log(res);
          if(res.role == "TEACHER"){
            this.router.navigate(['/borrow']);
          }else{
            this.router.navigate(['/dashboard']);
          }
          const userData = {
            token: res.user.token,
            fullName: res.user.fullname,
            role: res.user.role,
            userId: res.user.userId,
            email: res.user.email
          };
          localStorage.setItem('currentUser', JSON.stringify(userData));
          localStorage.setItem('token', res.token);
          localStorage.setItem('fullname',res.fullname);
          localStorage.setItem("role",res.role)
          localStorage.setItem('userId',res.userId)
        },
        error : (err)=>{
          this.isloading = false
          console.error('Đăng nhập thất bại:', err);
          this.toastr.error("Sai email hoặc mật khẩu!")
        }
      })
    }

    register(){
      this.emailExists = false;

      if (this.PassWord !== this.ConfirmPass) {
        alert('Mật khẩu xác nhận không khớp');
        return;
      }
      const user = {
        fullName: this.FullName,
        email: this.Email,
        passwordHash: this.PassWord
      };

      if (this.isloading) return;
      this.isloading = true;

      this.userservice.register(user).subscribe({
        next : (res) =>{
          this.isloading  = false
          alert('Đăng ký thành công');
          console.log(res);
          this.Showlogin = true;
        },
        error : (err)=>{
          this.emailExists = true;
          console.error('lỗi:', err);
          alert('Đăng kí thất bại ');
        }
      })
    }
}
