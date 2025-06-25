package com.ParQ.ParQ.repository;

import com.ParQ.ParQ.entity.ParkingOper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ParkingOperRepository extends JpaRepository<ParkingOper, Long> {
    @Query("SELECT o FROM ParkingOper o WHERE o.matchingId = :matchingId")
    List<ParkingOper> findByMatchingId(@Param("matchingId") String matchingId);
} 