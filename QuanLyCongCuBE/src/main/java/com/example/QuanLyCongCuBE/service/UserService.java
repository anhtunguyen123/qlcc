package com.example.QuanLyCongCuBE.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.QuanLyCongCuBE.AOP.LogAction;
import com.example.QuanLyCongCuBE.model.User;
import com.example.QuanLyCongCuBE.reponsitory.UserReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserReponsitory userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @LogAction(action = "Dăng kí tài khoản", actionType = "REGISTER")
    public User Register( User userinfo){
        if(userRepository.findByEmail(userinfo.getEmail()).isPresent()){
            throw new RuntimeException("Email da ton tai");
        }
        String encodingPassword = passwordEncoder.encode(userinfo.getPasswordHash());

        User user = new User();
        user.setFullName(userinfo.getFullName());
        user.setEmail(userinfo.getEmail());
        user.setPasswordHash(encodingPassword);
        user.setRole(User.Role.TEACHER);
        System.out.println("Dang ki thanh cong");
        return userRepository.save(user);
    }

    @LogAction(action = "Đăng nhập tài khoản", actionType = "LOGIN")
    public User Login(String email, String password){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            User userIF = user.get();
            if(passwordEncoder.matches(password, userIF.getPasswordHash())){
                System.out.println("Dang nhap thanh cong ");
                return userIF;
            }
            else{
                throw new RuntimeException("Sai mat khau");
            }
        }
        else{
            throw new RuntimeException("Email khong ton tai");
        }
    }

    @LogAction(action = "Cập nhập thông tin", actionType = "UPDATE")
    public User UpdateUser ( User userUpdate){
        User userExisting = userRepository.findById(userUpdate.getUserId())
        .orElseThrow(() -> new RuntimeException("user khong ton tai"));

        userExisting.setFullName(userUpdate.getFullName());
        userExisting.setEmail(userUpdate.getEmail());

      return userRepository.save(userExisting);
    }
    
    @LogAction(action = "Xóa tài khoản", actionType = "DELETE")
    public void DeleteUser (Long id){
         userRepository.deleteById(id);
         System.out.println("Xoa thanh cong " + id);
    }

    public List<User> getAllUser (){
        List<User> listUser = userRepository.findAll();
            return listUser;
   }


   public List<User> findUserBykeyword (String keyword){
        List<User> listUser = userRepository.findByFullNameContainingIgnoreCase(keyword);
            return listUser;
   }

    public UserDetails loadUserByUsername(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities("ROLE_" + user.getRole().name()) 
                .build();
    }
}
