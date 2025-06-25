package com.ParQ.ParQ.repository;

import com.ParQ.ParQ.entity.ParkingFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ParkingFacilityRepository extends JpaRepository<ParkingFacility, Long> {
    @Query("SELECT f FROM ParkingFacility f WHERE f.matchingId = :matchingId")
    List<ParkingFacility> findByMatchingId(@Param("matchingId") String matchingId);
} 