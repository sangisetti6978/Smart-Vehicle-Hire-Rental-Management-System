package com.vehiclerental.controller;

import com.vehiclerental.dto.BookingDTO;
import com.vehiclerental.model.Booking;
import com.vehiclerental.model.Shop;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.service.BookingService;
import com.vehiclerental.service.ShopService;
import com.vehiclerental.service.UserService;
import com.vehiclerental.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/owner")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('OWNER')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Owner", description = "Vehicle owner management APIs")
public class OwnerController {
    
    @Autowired
    private ShopService shopService;
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserService userService;
    
    // Shop Management
    @PostMapping("/shops")
    @Operation(summary = "Create shop", description = "Create a new shop")
    public ResponseEntity<Shop> createShop(@RequestBody Shop shop, Authentication auth) {
        String email = auth.getName();
        Long ownerId = userService.getUserByEmail(email).getId();
        shop.setOwnerId(ownerId);
        return ResponseEntity.ok(shopService.createShop(shop));
    }
    
    @GetMapping("/shops")
    @Operation(summary = "Get my shops", description = "Get all shops owned by the current owner")
    public ResponseEntity<List<Shop>> getMyShops(Authentication auth) {
        String email = auth.getName();
        Long ownerId = userService.getUserByEmail(email).getId();
        return ResponseEntity.ok(shopService.getShopsByOwner(ownerId));
    }
    
    @GetMapping("/shops/{id}")
    @Operation(summary = "Get shop details", description = "Get detailed information about a specific shop")
    public ResponseEntity<Shop> getShopById(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.getShopById(id));
    }
    
    @PutMapping("/shops/{id}")
    @Operation(summary = "Update shop", description = "Update shop information")
    public ResponseEntity<Shop> updateShop(@PathVariable Long id, @RequestBody Shop shop) {
        return ResponseEntity.ok(shopService.updateShop(id, shop));
    }
    
    @PutMapping("/shops/{id}/toggle-status")
    @Operation(summary = "Toggle shop status", description = "Enable or disable a shop")
    public ResponseEntity<Shop> toggleShopStatus(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.toggleShopStatus(id));
    }
    
    @DeleteMapping("/shops/{id}")
    @Operation(summary = "Delete shop", description = "Delete a shop")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.ok().build();
    }
    
    // Vehicle Management
    @PostMapping("/vehicles")
    @Operation(summary = "Add vehicle", description = "Add a new vehicle to a shop")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
    }
    
    @GetMapping("/shops/{shopId}/vehicles")
    @Operation(summary = "Get shop vehicles", description = "Get all vehicles in a specific shop")
    public ResponseEntity<List<com.vehiclerental.dto.VehicleDTO>> getShopVehicles(@PathVariable Long shopId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByShop(shopId));
    }
    
    @PutMapping("/vehicles/{id}")
    @Operation(summary = "Update vehicle", description = "Update vehicle information")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicle));
    }
    
    @PutMapping("/vehicles/{id}/availability")
    @Operation(summary = "Update vehicle availability", description = "Enable or disable vehicle availability")
    public ResponseEntity<Vehicle> updateVehicleAvailability(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        Boolean isAvailable = payload.get("isAvailable");
        return ResponseEntity.ok(vehicleService.updateVehicleAvailability(id, isAvailable));
    }
    
    @PutMapping("/vehicles/{id}/toggle-status")
    @Operation(summary = "Toggle vehicle status", description = "Activate or deactivate a vehicle listing")
    public ResponseEntity<Vehicle> toggleVehicleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.toggleVehicleStatus(id));
    }
    
    @DeleteMapping("/vehicles/{id}")
    @Operation(summary = "Delete vehicle", description = "Delete a vehicle listing")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok().build();
    }
    
    // Booking Management
    @GetMapping("/shops/{shopId}/bookings")
    @Operation(summary = "Get shop bookings", description = "Get all bookings for a specific shop")
    public ResponseEntity<List<BookingDTO>> getShopBookings(@PathVariable Long shopId) {
        return ResponseEntity.ok(bookingService.getBookingsByShop(shopId));
    }
    
    @PutMapping("/bookings/{id}/accept")
    @Operation(summary = "Accept booking", description = "Accept a pending booking request")
    public ResponseEntity<Booking> acceptBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.acceptBooking(id));
    }
    
    @PutMapping("/bookings/{id}/reject")
    @Operation(summary = "Reject booking", description = "Reject a pending booking request")
    public ResponseEntity<Booking> rejectBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.rejectBooking(id));
    }
    
    @PutMapping("/bookings/{id}/complete")
    @Operation(summary = "Complete booking", description = "Mark a booking as completed")
    public ResponseEntity<Booking> completeBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.completeBooking(id));
    }
}
