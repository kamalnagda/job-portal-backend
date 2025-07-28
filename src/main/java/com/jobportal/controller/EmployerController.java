package com.jobportal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.dto.ApplicantResponse;
import com.jobportal.dto.EmployerLoginRequest;
import com.jobportal.dto.EmployerRegisterRequest;
import com.jobportal.dto.EmployerResponse;
import com.jobportal.dto.EmployerUpdateRequest;
import com.jobportal.dto.JobPostRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.dto.JobSeekerResponse;
import com.jobportal.dto.JobUpdateRequest;
import com.jobportal.dto.ResetPasswordRequest;
import com.jobportal.model.Employer;
import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.service.EmployerService;
import com.jobportal.service.JobApplicationService;
import com.jobportal.service.JobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employers")
public class EmployerController {

	@Autowired
	private EmployerService employerService;

	@Autowired
	private JobService jobService;

	@Autowired
	private JobApplicationService jobApplicationService;

	@PostMapping("/register")
	public ResponseEntity<?> registerEmployer(@Valid @ModelAttribute EmployerRegisterRequest request) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body("success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Employer registration failed: " + e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginEmployer(@Valid @RequestBody EmployerLoginRequest request) {
		try {
			Employer employer = employerService.loginEmployer(request.getEmail(), request.getPassword());

			EmployerResponse response = new EmployerResponse();
			response.setId(employer.getId());
			response.setName(employer.getName());
			response.setEmail(employer.getEmail());
			response.setCompanyName(employer.getCompanyName());
			response.setCompanyLogo(employer.getCompanyLogo());

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
		}
	}

	// Post job
	@PostMapping("/{employerId}/jobs")
	public ResponseEntity<?> postJob(@PathVariable Long employerId, @Valid @RequestBody JobPostRequest request) {
		try {
			Job job = jobService.postJob(employerId, request);

			JobResponse response = new JobResponse();
			response.setId(job.getId());
			response.setTitle(job.getTitle());
			response.setDescription(job.getDescription());
			response.setLocation(job.getLocation());
			response.setSkillsRequired(job.getSkillsRequired());
			response.setSalary(job.getSalary());
			response.setPostedAt(job.getPostedAt());

			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to post job: " + e.getMessage());
		}
	}

//	update job
	@PutMapping("/{employerId}/jobs/{jobId}")
	public ResponseEntity<?> updateJob(@PathVariable Long employerId, @PathVariable Long jobId,
			@Valid @RequestBody JobUpdateRequest request) {
		try {
			Job updatedJob = jobService.updateJob(employerId, jobId, request);

			JobResponse response = new JobResponse();
			response.setId(updatedJob.getId());
			response.setTitle(updatedJob.getTitle());
			response.setDescription(updatedJob.getDescription());
			response.setLocation(updatedJob.getLocation());
			response.setSkillsRequired(updatedJob.getSkillsRequired());
			response.setSalary(updatedJob.getSalary());
			response.setPostedAt(updatedJob.getPostedAt());

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update job: " + e.getMessage());
		}
	}

	@DeleteMapping("/{employerId}/jobs/{jobId}")
	public ResponseEntity<?> deleteJob(@PathVariable Long employerId, @PathVariable Long jobId) {
		try {
			jobService.deleteJob(employerId, jobId);
			return ResponseEntity.ok("Job deleted successfully");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete job: " + e.getMessage());
		}
	}

	// list of all post
	@GetMapping("/{employerId}/jobs")
	public ResponseEntity<?> getJobsByEmployer(@PathVariable Long employerId) {
		try {
			List<Job> jobs = jobService.getJobsByEmployer(employerId);

			List<JobResponse> responseList = jobs.stream().map(job -> {
				JobResponse response = new JobResponse();
				response.setId(job.getId());
				response.setTitle(job.getTitle());
				response.setDescription(job.getDescription());
				response.setLocation(job.getLocation());
				response.setSkillsRequired(job.getSkillsRequired());
				response.setSalary(job.getSalary());
				response.setPostedAt(job.getPostedAt());
				return response;
			}).toList();

			return ResponseEntity.ok(responseList);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch jobs: " + e.getMessage());
		}
	}

	@GetMapping("/{employerId}/jobs/{jobId}/applicants")
	public ResponseEntity<?> getApplicantsForJob(@PathVariable Long employerId, @PathVariable Long jobId) {
		try {
			List<ApplicantResponse> applicants = jobApplicationService.getApplicantsForJob(employerId, jobId);
			return ResponseEntity.ok(applicants);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch applicants: " + e.getMessage());
		}
	}

	@GetMapping("/{employerId}/jobs/{jobId}")
	public ResponseEntity<?> getJobForEmployer(@PathVariable Long employerId, @PathVariable Long jobId) {

		try {
			Job job = jobService.getJobById(employerId, jobId);

			JobResponse response = new JobResponse();

			response.setId(job.getId());
			response.setTitle(job.getTitle());
			response.setDescription(job.getDescription());
			response.setLocation(job.getLocation());
			response.setSkillsRequired(job.getSkillsRequired());
			response.setSalary(job.getSalary());
			response.setPostedAt(job.getPostedAt());

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch jobs: " + e.getMessage());
		}
	}

	// view profile
	@GetMapping("profile/{id}")
	public ResponseEntity<?> getEmployerProfileById(@PathVariable Long id) {

		Employer employer = employerService.getEmployerById(id);

		if (employer == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		EmployerResponse response = new EmployerResponse();
		response.setId(employer.getId());
		response.setName(employer.getName());
		response.setCompanyName(employer.getCompanyName());
		response.setEmail(employer.getEmail());
		response.setCompanyLogo(employer.getCompanyLogo());
		return ResponseEntity.ok(response);
	}

	// update profile
	@PutMapping("/{id}")
	public ResponseEntity<?> updateEmployerProfile(@PathVariable Long id,
			@ModelAttribute EmployerUpdateRequest request) {
		try {
			Employer updated = employerService.updateEmployer(id, request);
			return ResponseEntity.ok(updated);
		} catch (org.springframework.web.multipart.MaxUploadSizeExceededException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(" File too large. Please upload a file within the allowed size limit.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update profile: " + e.getMessage());
		}
	}

//	update Password
	@PutMapping("/{id}/reset-password")
	public ResponseEntity<?> resetPassword(@PathVariable Long id,@Valid @RequestBody ResetPasswordRequest request) {
		try {
			employerService.resetPassword(id, request.getOldPassword(), request.getNewPassword());
			return ResponseEntity.ok("Password updated successfully.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

}
