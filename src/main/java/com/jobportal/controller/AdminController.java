package com.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.dto.AdminLoginRequest;
import com.jobportal.dto.AdminLoginResponse;
import com.jobportal.model.Admin;
import com.jobportal.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody AdminLoginRequest request) {
        try {
            Admin admin = adminService.loginAdmin(request.getEmail(), request.getPassword());

            AdminLoginResponse response = new AdminLoginResponse();
            response.setId(admin.getId());
            response.setName(admin.getName());
            response.setEmail(admin.getEmail());
            response.setMessage("Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login failed: " + e.getMessage());
        }
    }
}

