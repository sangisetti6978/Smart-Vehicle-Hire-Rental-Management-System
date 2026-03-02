package com.vehiclerental.repository;

import com.vehiclerental.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByShopId(Long shopId);
    
    List<Vehicle> findByVehicleType(Vehicle.VehicleType vehicleType);
    
    List<Vehicle> findByBrand(String brand);
    
    List<Vehicle> findByIsAvailable(Boolean isAvailable);
    
    List<Vehicle> findByIsActive(Boolean isActive);
    
    @Query("SELECT v FROM Vehicle v WHERE v.isActive = true AND v.isAvailable = true " +
           "AND (:type IS NULL OR v.vehicleType = :type) " +
           "AND (:brand IS NULL OR LOWER(v.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) " +
           "AND (:minPrice IS NULL OR v.pricePerDay >= :minPrice) " +
           "AND (:maxPrice IS NULL OR v.pricePerDay <= :maxPrice)")
    List<Vehicle> searchVehicles(
        @Param("type") Vehicle.VehicleType type,
        @Param("brand") String brand,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice
    );
    
    @Query("SELECT v FROM Vehicle v JOIN Shop s ON v.shopId = s.id " +
           "WHERE v.isActive = true AND v.isAvailable = true " +
           "AND (:city IS NULL OR LOWER(s.city) = LOWER(:city))")
    List<Vehicle> findByCity(@Param("city") String city);

    @Query("SELECT v FROM Vehicle v JOIN Shop s ON v.shopId = s.id " +
           "WHERE v.isActive = true AND v.isAvailable = true " +
           "AND (:state IS NULL OR LOWER(s.state) = LOWER(:state)) " +
           "AND (:city IS NULL OR LOWER(s.city) = LOWER(:city)) " +
           "AND (:area IS NULL OR LOWER(s.area) = LOWER(:area) OR LOWER(s.address) LIKE LOWER(CONCAT('%', :area, '%'))) " +
           "AND (:type IS NULL OR v.vehicleType = :type) " +
           "AND (:minPrice IS NULL OR v.pricePerDay >= :minPrice) " +
           "AND (:maxPrice IS NULL OR v.pricePerDay <= :maxPrice)")
    List<Vehicle> advancedSearch(
        @Param("state") String state,
        @Param("city") String city,
        @Param("area") String area,
        @Param("type") Vehicle.VehicleType type,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice
    );
}
