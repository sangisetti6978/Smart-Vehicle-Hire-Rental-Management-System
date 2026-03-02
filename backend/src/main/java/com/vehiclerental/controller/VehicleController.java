package com.vehiclerental.controller;

import com.vehiclerental.dto.SearchRequest;
import com.vehiclerental.dto.VehicleDTO;
import com.vehiclerental.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/vehicles")
@CrossOrigin(origins = "*")
@Tag(name = "Public Vehicles", description = "Public vehicle browsing APIs")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @GetMapping
    @Operation(summary = "Browse all vehicles", description = "Get list of all available vehicles (no authentication required)")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "View vehicle details", description = "Get detailed information about a specific vehicle (no authentication required)")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleDTOById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Public search vehicles", description = "Search and filter vehicles by location, type, brand, and price (no authentication required)")
    public ResponseEntity<List<VehicleDTO>> searchVehiclesPublic(@ModelAttribute SearchRequest request) {
        return ResponseEntity.ok(vehicleService.searchVehicles(request));
    }
}
