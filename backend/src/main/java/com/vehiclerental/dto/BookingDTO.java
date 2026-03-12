package com.vehiclerental.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long vehicleId;
    private String vehicleName;
    private Long shopId;
    private String shopName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalHours;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime bookingDate;
    private LocalDateTime confirmationDeadline;
    private String notes;
    private String paymentStatus;
    private Long paymentId;
    private String paymentMethod;
    private String transactionId;
    private String paymentDate;
}
