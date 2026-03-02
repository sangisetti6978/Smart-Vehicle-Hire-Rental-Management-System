package com.vehiclerental.controller;

import com.vehiclerental.dto.BookingDTO;
import com.vehiclerental.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Bookings", description = "Booking management APIs")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get vehicle bookings", description = "Get all bookings for a specific vehicle")
    public ResponseEntity<List<BookingDTO>> getVehicleBookings(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(bookingService.getBookingsByVehicle(vehicleId));
    }
}
