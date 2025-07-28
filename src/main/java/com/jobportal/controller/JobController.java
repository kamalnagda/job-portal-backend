package com.jobportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.dto.JobResponse;
import com.jobportal.model.Job;
import com.jobportal.service.JobService;

@RestController
@RequestMapping("/api")
public class JobController {
	
	@Autowired
	JobService jobService;

    @GetMapping("/jobs")
    public ResponseEntity<?> getAllJobs() {
        try {
            List<Job> jobs = jobService.getAllJobs();

            List<JobResponse> responseList = jobs.stream().map(job -> {
                JobResponse response = new JobResponse();
                response.setId(job.getId());
                response.setTitle(job.getTitle());
                response.setDescription(job.getDescription());
                response.setLocation(job.getLocation());
                response.setSkillsRequired(job.getSkillsRequired());
                response.setSalary(job.getSalary());
                response.setPostedAt(job.getPostedAt());
                response.setCompanyLogo(job.getEmployer().getCompanyLogo());
                return response;
            }).toList();

            return ResponseEntity.ok(responseList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to fetch jobs: " + e.getMessage());
        }
    }
    
    
    @GetMapping("/jobs/search")
    public ResponseEntity<?> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skills) {
        try {
            List<Job> jobs = jobService.searchJobs(title, location, skills);

            List<JobResponse> responseList = jobs.stream().map(job -> {
                JobResponse response = new JobResponse();
                response.setId(job.getId());
                response.setTitle(job.getTitle());
                response.setDescription(job.getDescription());
                response.setLocation(job.getLocation());
                response.setSkillsRequired(job.getSkillsRequired());
                response.setSalary(job.getSalary());
                response.setPostedAt(job.getPostedAt());
                response.setCompanyLogo(job.getEmployer().getCompanyLogo());
                
                
                return response;
            }).toList();

            return ResponseEntity.ok(responseList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to search jobs: " + e.getMessage());
        }
    }
    
    @GetMapping("/job/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
    	
    	try {
            Job job = jobService.getJobById(id);
            JobResponse response = new JobResponse();
            response.setId(job.getId());
            response.setTitle(job.getTitle());
            response.setDescription(job.getDescription());
            response.setLocation(job.getLocation());
            response.setSkillsRequired(job.getSkillsRequired());
            response.setSalary(job.getSalary());
            response.setPostedAt(job.getPostedAt());
            response.setCompanyLogo(job.getEmployer().getCompanyLogo());
            
            return ResponseEntity.ok(response);
    	}
    	catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to search jobs: " + e.getMessage());
		}
    	

    }


}
