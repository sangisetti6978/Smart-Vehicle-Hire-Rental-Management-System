package com.vehiclerental.config;

import com.vehiclerental.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    
    @Autowired
    private BookingService bookingService;
    
    /**
     * Auto-cancel pending bookings that have expired (past confirmation deadline)
     * Runs every minute to check for expired bookings
     */
    @Scheduled(fixedRate = 60000) // Run every 60 seconds
    public void autoCancelExpiredBookings() {
        bookingService.autoCancelExpiredBookings();
    }
}
