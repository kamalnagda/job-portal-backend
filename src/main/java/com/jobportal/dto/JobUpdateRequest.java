package com.jobportal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobUpdateRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    private String location;
    private String skillsRequired;
    private Double salary;

}