package com.jobportal.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobSeekerUpdateRequest {
    @NotBlank
    private String name;

    private MultipartFile profilePhoto;

    private MultipartFile resume;

}

