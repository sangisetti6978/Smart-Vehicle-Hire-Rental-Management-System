package com.vehiclerental.repository;

import com.vehiclerental.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerId(Long customerId);
    
    List<Booking> findByVehicleId(Long vehicleId);
    
    List<Booking> findByShopId(Long shopId);
    
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.status = 'PENDING' " +
           "AND b.confirmationDeadline < :currentTime")
    List<Booking> findExpiredPendingBookings(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT b FROM Booking b WHERE b.vehicleId = :vehicleId " +
           "AND b.status IN ('PENDING', 'ACCEPTED') " +
           "AND ((b.startTime <= :endTime AND b.endTime >= :startTime))")
    List<Booking> findOverlappingBookings(
        @Param("vehicleId") Long vehicleId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.shopId = :shopId " +
           "AND b.status = 'COMPLETED'")
    Long countCompletedBookingsByShop(@Param("shopId") Long shopId);
}
