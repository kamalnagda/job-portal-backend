package com.jobportal.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApplicantResponse {

	// for user
    private Long applicantId;
    private String name;
    private String email;
    private String profilePhoto;
    private String resume;
    
    //for application
    private Long applicationId;
    private String status; 
    private LocalDateTime appliedDate;
	
	
}
