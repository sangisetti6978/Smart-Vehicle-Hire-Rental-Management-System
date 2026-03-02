package com.vehiclerental.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SearchRequest {
    private String state;
    private String city;
    private String area;
    private String vehicleType;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
