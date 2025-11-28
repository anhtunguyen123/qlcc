package com.example.QuanLyCongCuBE.reponsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.QuanLyCongCuBE.model.User;

public interface UserReponsitory extends JpaRepository<User,Long>{
    Optional<User> findByEmail (String email);
    Optional<User> findById(Long id);
    List<User> findByFullNameContainingIgnoreCase(String keyword);
}
