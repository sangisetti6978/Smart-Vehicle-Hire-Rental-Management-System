package com.vehiclerental.repository;

import com.vehiclerental.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByVehicleId(Long vehicleId);
    
    List<Review> findByCustomerId(Long customerId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.vehicleId = :vehicleId")
    Double findAverageRatingByVehicle(@Param("vehicleId") Long vehicleId);
}
