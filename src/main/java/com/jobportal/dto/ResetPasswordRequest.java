package com.jobportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data	
public class ResetPasswordRequest {
	
    @NotBlank(message = "password is required")
    private String oldPassword;
    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "password is required")
    private String newPassword;
}
