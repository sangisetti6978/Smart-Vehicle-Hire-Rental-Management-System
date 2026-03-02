package com.vehiclerental.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {
    
    @Autowired
    private BookingService bookingService;
    
    /**
     * Automatically cancel expired pending bookings every 10 minutes
     * This ensures that bookings not confirmed within 2 hours are cancelled
     */
    @Scheduled(fixedDelay = 600000) // Run every 10 minutes
    public void cancelExpiredBookings() {
        System.out.println("Running scheduled task to cancel expired bookings...");
        bookingService.autoCancelExpiredBookings();
        System.out.println("Expired bookings cancellation task completed.");
    }
}
