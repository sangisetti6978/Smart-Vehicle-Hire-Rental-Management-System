package com.vehiclerental.controller;

import com.vehiclerental.model.User;
import com.vehiclerental.model.Shop;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.service.UserService;
import com.vehiclerental.service.ShopService;
import com.vehiclerental.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Admin management APIs")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ShopService shopService;
    
    @Autowired
    private VehicleService vehicleService;
    
    // User Management
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Get list of all registered users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/users/{id}")
    @Operation(summary = "Get user details", description = "Get detailed information about a specific user")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    @GetMapping("/owners/unverified")
    @Operation(summary = "Get unverified owners", description = "Get list of owners pending verification")
    public ResponseEntity<List<User>> getUnverifiedOwners() {
        return ResponseEntity.ok(userService.getUnverifiedOwners());
    }
    
    @PutMapping("/users/{id}/verify")
    @Operation(summary = "Verify user", description = "Verify a user account (typically for owners)")
    public ResponseEntity<User> verifyUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.verifyUser(id));
    }
    
    @PutMapping("/users/{id}/toggle-status")
    @Operation(summary = "Toggle user status", description = "Activate or deactivate a user account")
    public ResponseEntity<User> toggleUserStatus(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleUserStatus(id));
    }
    
    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete user", description = "Delete a user account")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    
    // Shop Management
    @GetMapping("/shops")
    @Operation(summary = "Get all shops", description = "Get list of all shops")
    public ResponseEntity<List<Shop>> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }
    
    @DeleteMapping("/shops/{id}")
    @Operation(summary = "Delete shop", description = "Delete a shop (for removing fake listings)")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/shops/{id}/toggle-status")
    @Operation(summary = "Toggle shop status", description = "Enable or disable a shop")
    public ResponseEntity<Shop> toggleShopStatus(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.toggleShopStatus(id));
    }
    
    // Vehicle Management
    @GetMapping("/vehicles")
    @Operation(summary = "Get all vehicles", description = "Get list of all vehicles")
    public ResponseEntity<List<com.vehiclerental.dto.VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }
    
    @DeleteMapping("/vehicles/{id}")
    @Operation(summary = "Delete vehicle", description = "Delete a vehicle listing (for removing fake listings)")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/vehicles/{id}/toggle-status")
    @Operation(summary = "Toggle vehicle status", description = "Activate or deactivate a vehicle listing")
    public ResponseEntity<Vehicle> toggleVehicleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.toggleVehicleStatus(id));
    }
}
