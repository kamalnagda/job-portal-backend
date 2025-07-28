package com.jobportal.dto;

import lombok.Data;

@Data
public class AdminLoginResponse {

    private Long id;
    private String name;
    private String email;
    private String message;

    // Getters and Setters
}