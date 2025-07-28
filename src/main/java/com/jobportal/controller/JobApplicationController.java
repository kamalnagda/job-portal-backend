package com.jobportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.dto.JobApplicationResponse;
import com.jobportal.service.JobApplicationService;

@RestController
@RequestMapping("/api")
public class JobApplicationController {

	@Autowired
	private JobApplicationService jobApplicationService;

	@PostMapping("/{jobSeekerId}/apply/{jobId}")
	public ResponseEntity<?> applyForJob(@PathVariable Long jobSeekerId, @PathVariable Long jobId) {
		try {
			jobApplicationService.applyForJob(jobSeekerId, jobId);
			return ResponseEntity.ok("Application submitted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to apply for job: " + e.getMessage());
		}
	}

	@GetMapping("/{jobSeekerId}/applications")
	public ResponseEntity<?> getMyApplications(@PathVariable Long jobSeekerId) {
		try {
			List<JobApplicationResponse> applications = jobApplicationService.getApplicationsByJobSeeker(jobSeekerId);
			return ResponseEntity.ok(applications);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to fetch applications: " + e.getMessage());
		}
	}

	@GetMapping("/{jobSeekerId}/application/{appId}")
	public ResponseEntity<?> getMyApplicationbyId(@PathVariable Long jobSeekerId, @PathVariable Long appId) {
		try {

			System.out.print("hello");
			JobApplicationResponse application = jobApplicationService.getApplicationsById(jobSeekerId, appId);
			return ResponseEntity.ok(application);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to fetch applications: " + e.getMessage());
		}
	}

	// update job status employer
	@PutMapping("/{employerId}/status/{applicationId}")
	public ResponseEntity<?> updateApplicationStatus(@PathVariable Long employerId, @PathVariable Long applicationId, @RequestParam String status) {
		try {
			jobApplicationService.updateStatus(employerId,applicationId, status);
			return ResponseEntity.ok("Status updated successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
		}
	}

}
