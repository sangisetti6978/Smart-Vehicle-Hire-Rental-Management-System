package com.vehiclerental.repository;

import com.vehiclerental.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByOwnerId(Long ownerId);
    
    List<Shop> findByCity(String city);
    
    List<Shop> findByIsActive(Boolean isActive);
}
