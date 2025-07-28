package com.jobportal.dto;

import lombok.Data;

@Data
public class JobSeekerResponse {
    private Long id;
    private String name;
    private String email;
    private String profilePhoto;
    private String resume;
    private String role ="JOB_SEEKER";
}
