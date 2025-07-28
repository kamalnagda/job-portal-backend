package com.jobportal.dto;

import lombok.Data;

@Data
public class EmployerResponse {

    private Long id;
    private String name;
    private String email;
    private String companyName;
    private String companyLogo;
    private String role="EMPLOYER";

    // Getters and Setters
}