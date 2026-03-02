package com.vehiclerental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private String email;
    private String fullName;
    private String role;
    private Long userId;
    
    public AuthResponse(String token, String email, String fullName, String role, Long userId) {
        this.token = token;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.userId = userId;
    }
}
