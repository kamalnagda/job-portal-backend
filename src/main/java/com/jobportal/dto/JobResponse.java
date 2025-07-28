package com.jobportal.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String skillsRequired;
    private Double salary;
    private String companyLogo;
    private LocalDateTime postedAt;
    
}

