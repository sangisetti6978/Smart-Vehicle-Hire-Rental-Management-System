package com.vehiclerental.service;

import com.vehiclerental.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendBookingNotificationToOwner(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("owner@example.com"); // Replace with actual owner email
            message.setSubject("New Booking Request - Vehicle Rental Platform");
            message.setText(String.format(
                "You have received a new booking request!\n\n" +
                "Booking ID: %d\n" +
                "Vehicle ID: %d\n" +
                "Start Time: %s\n" +
                "End Time: %s\n" +
                "Total Price: $%.2f\n\n" +
                "Please accept or reject this booking within 2 hours.",
                booking.getId(),
                booking.getVehicleId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getTotalPrice()
            ));
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    
    public void sendBookingConfirmationToCustomer(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("customer@example.com"); // Replace with actual customer email
            message.setSubject("Booking Confirmed - Vehicle Rental Platform");
            message.setText(String.format(
                "Your booking has been confirmed!\n\n" +
                "Booking ID: %d\n" +
                "Vehicle ID: %d\n" +
                "Start Time: %s\n" +
                "End Time: %s\n" +
                "Total Price: $%.2f\n\n" +
                "Thank you for using our service!",
                booking.getId(),
                booking.getVehicleId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getTotalPrice()
            ));
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    
    public void sendBookingCancellationNotification(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("customer@example.com"); // Replace with actual customer email
            message.setSubject("Booking Cancelled - Vehicle Rental Platform");
            message.setText(String.format(
                "Your booking has been automatically cancelled due to no response from the owner.\n\n" +
                "Booking ID: %d\n" +
                "Vehicle ID: %d\n\n" +
                "You can make a new booking at any time.",
                booking.getId(),
                booking.getVehicleId()
            ));
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
