package com.jobportal.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployerUpdateRequest {
	@NotBlank
    private String name;
	@NotBlank
    private String companyName;
    private MultipartFile companyLogo;
}
