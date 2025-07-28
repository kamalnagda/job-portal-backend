package com.jobportal.controller;

import com.jobportal.dto.JobSeekerRegisterRequest;
import com.jobportal.dto.JobSeekerLoginRequest;
import com.jobportal.dto.JobSeekerResponse;
import com.jobportal.dto.JobSeekerUpdateRequest;
import com.jobportal.dto.ResetPasswordRequest;
import com.jobportal.model.JobSeeker;
import com.jobportal.service.JobSeekerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobseekers")
public class JobSeekerController {

	@Autowired
	private JobSeekerService jobSeekerService;

	// Register JobSeeker
	@PostMapping("/register")
	public ResponseEntity<?> registerJobSeeker(@Valid @ModelAttribute JobSeekerRegisterRequest request) {
		try {
			jobSeekerService.registerJobSeeker(request);

			return ResponseEntity.status(HttpStatus.CREATED).body("success");

		} catch (Exception e) {
			// Catch and return any error
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
		}
	}

	// Login JobSeeker
	@PostMapping("/login")
	public ResponseEntity<?> loginJobSeeker(@Valid @RequestBody JobSeekerLoginRequest request) {
		try {
			JobSeekerResponse user = jobSeekerService.loginJobSeeker(request.getEmail(), request.getPassword());
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
		}
	}

	// get profile
	@GetMapping("/{id}")
	public ResponseEntity<JobSeekerResponse> getJobSeekerProfile(@PathVariable Long id) {
		JobSeeker jobSeeker = jobSeekerService.getJobSeekerById(id);

		if (jobSeeker == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
        try
        {
        	JobSeekerResponse response = new JobSeekerResponse();
        	response.setId(jobSeeker.getId());
        	response.setName(jobSeeker.getName());
        	response.setEmail(jobSeeker.getEmail());
        	response.setProfilePhoto(jobSeeker.getProfilePhoto());
        	response.setResume(jobSeeker.getResume());
        	
        	return ResponseEntity.ok(response);
        }
        catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// update profile

	@PutMapping("/{id}")
	public ResponseEntity<?> updateJobSeeker(@PathVariable Long id, @ModelAttribute JobSeekerUpdateRequest request) {
		try {
			JobSeeker updated = jobSeekerService.updateJobSeeker(id, request);
			return ResponseEntity.ok(updated);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed: " + e.getMessage());
		}
	}

	// reset password
	@PutMapping("/{id}/reset-password")
	public ResponseEntity<?> resetJobSeekerPassword(@PathVariable Long id,@Valid @RequestBody ResetPasswordRequest request) {

		try {
			jobSeekerService.resetPassword(id, request.getOldPassword(), request.getNewPassword());
			return ResponseEntity.ok("Password updated successfully.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// health-check
	@PostMapping("/health-check")
	public String healthCheck() {
		return "all good";
	}

}