package com.jobportal.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class JobSeekerRegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "password is required")
    private String password;
    
    private MultipartFile resume;
    private MultipartFile profilePhoto;
    
}
