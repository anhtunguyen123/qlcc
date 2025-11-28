package com.example.QuanLyCongCuBE.reponsitory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.QuanLyCongCuBE.model.Borrow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow,Long>{

    @Query("SELECT COUNT(b) FROM Borrow b WHERE b.status = 'BORROWED'")
    long countBorrowedDevices();
    
    List<Borrow> findByUserUserIdAndStatus(Long userId, String status);

    Optional<Borrow> findByReservationReservationId (Long ReservationId);
    
    @Query("""
        SELECT b FROM Borrow b 
        JOIN FETCH b.device 
        JOIN FETCH b.user 
        WHERE b.status = 'BORROWED' 
        AND b.returnDue < :today
        ORDER BY b.returnDue ASC
    """)
    List<Borrow> findOverdueBorrows(@Param("today") LocalDateTime today);

    @Query("""
    SELECT COUNT(b) FROM Borrow b
    WHERE b.status = 'BORROWED'
    AND b.returnDue < :now
    """)
    long countOverdue(@Param("now") LocalDateTime now);

    @Query("""
    SELECT b FROM Borrow b
    WHERE b.status = 'BORROWED'
    AND b.returnDue > :now
    AND b.returnDue <= :deadline
    ORDER BY b.returnDue ASC
    """)
    List<Borrow> almostDue(
        @Param("now") LocalDateTime now,
        @Param("deadline") LocalDateTime deadline);


    @Query("""
        SELECT COUNT(b) FROM Borrow b
        WHERE b.status = 'RETURNED'
        """)
    long countReturnBorrow();
}
