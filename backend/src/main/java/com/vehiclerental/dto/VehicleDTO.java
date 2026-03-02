package com.vehiclerental.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleDTO {
    private Long id;
    private Long shopId;
    private String shopName;
    private String vehicleName;
    private String brand;
    private String model;
    private String vehicleType;
    private String registrationNumber;
    private String fuelType;
    private String transmission;
    private Integer seatingCapacity;
    private BigDecimal pricePerDay;
    private String description;
    private String imageUrl;
    private Boolean isAvailable;
    private Boolean isActive;
    private String city;
    private String state;
    private String area;
    private String address;
    private Double averageRating;
    private String ownerPhone;
    private String ownerName;
}
