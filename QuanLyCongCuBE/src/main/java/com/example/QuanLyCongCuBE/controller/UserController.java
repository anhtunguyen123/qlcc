package com.example.QuanLyCongCuBE.controller;
import com.example.QuanLyCongCuBE.model.User;
import com.example.QuanLyCongCuBE.service.JwtService;
import com.example.QuanLyCongCuBE.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
    
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<User> register (@RequestBody User user){
        User newUser = userService.Register(user);
            return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody User user ){

        User user1 = userService.Login(user.getEmail(),user.getPasswordHash());
        String token = jwtService.generateToken(user1.getEmail());
            return ResponseEntity.ok(Map.of(
                "message", "suscessfull",
                "token", token,
                "fullname", user1.getFullName(),
                "role",user1.getRole(),
                "userId",user1.getUserId(),
                "user",user1
        ));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser (@RequestBody User user ){
        User user1 = userService.UpdateUser(user);
        System.out.println("email" + user1.getEmail());
            return ResponseEntity.ok(Map.of(
                "message", "Cap nhap thanh cong",
                "user", user1
        ));
    }   

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete (@PathVariable("id") Long id ){
        userService.DeleteUser(id);
            return ResponseEntity.ok(Map.of(
                "message", "Xóa thành công"
        ));
    }

    @GetMapping("/getuser")
    public ResponseEntity<List<User>> getUser (){
        List<User> users = userService.getAllUser();
            return ResponseEntity.ok(users);
    }

    @GetMapping("/getUserBykeyword")
    public ResponseEntity<?> findUserBykeyWord(String keyWord){
        List<User> UserList = userService.findUserBykeyword(keyWord);
            return ResponseEntity.ok(UserList);
    }
}
