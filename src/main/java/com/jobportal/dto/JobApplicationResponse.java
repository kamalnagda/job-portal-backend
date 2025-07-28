package com.jobportal.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class JobApplicationResponse {
    private Long applicationId;
    private Long jobId;
    private String title;
    private String companyName;
    private String description;
    private String location;
    private String skillsRequired;
    private String salary;
    private LocalDateTime postedAt;

    private String status; 
    private LocalDateTime appliedAt;  // 👈 Optional: When applied

    // Getters and Setters
}
