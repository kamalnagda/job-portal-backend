package com.jobportal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobportal.dto.ApplicantResponse;
import com.jobportal.dto.JobApplicationResponse;
import com.jobportal.dto.JobResponse;
import com.jobportal.dto.JobSeekerResponse;
import com.jobportal.model.ApplicationStatus;
import com.jobportal.model.Employer;
import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.model.JobSeeker;
import com.jobportal.repository.EmployerRepository;
import com.jobportal.repository.JobApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.JobSeekerRepository;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    
    @Autowired
    private EmployerRepository employerRepository;


    //create a job application
    public void applyForJob(Long jobSeekerId, Long jobId) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job seeker not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        
        if (jobApplicationRepository.existsByJobSeekerAndJob(jobSeeker, job)) {
            throw new RuntimeException("You have already applied for this job");
        }

        JobApplication application = new JobApplication(jobSeeker, job);
        jobApplicationRepository.save(application);
    }
    
    //return  list of all application for perticular jobseeker
    public List<JobApplicationResponse> getApplicationsByJobSeeker(Long jobSeekerId) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job seeker not found"));

        List<JobApplication> applications = jobApplicationRepository.findByJobSeekerId(jobSeekerId);

        return applications.stream().map(app -> {
            Job job = app.getJob();
            JobApplicationResponse response = new JobApplicationResponse();
            response.setApplicationId(app.getId());
            response.setTitle(job.getTitle());
            response.setCompanyName(job.getEmployer().getCompanyName());
            response.setStatus(app.getStatus().toString());
            response.setDescription(job.getDescription());
            response.setLocation(job.getLocation());
            response.setSkillsRequired(job.getSkillsRequired());
            response.setSalary(job.getSalary());
            response.setPostedAt(job.getPostedAt());
            response.setAppliedAt(app.getAppliedAt());
            return response;
        }).toList();
    }
    
    //single job application
    public JobApplicationResponse getApplicationsById(Long jobSeekerId , long appId) {
    	
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job seeker not found"));

        JobApplication app= jobApplicationRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + appId));

            if (!app.getJobSeeker().getId().equals(jobSeekerId)) {
                throw new RuntimeException("You are not authorized to view this application.");
            }
            
            JobApplicationResponse response = new JobApplicationResponse();
            response.setApplicationId(app.getId());
            response.setTitle(app.getJob().getTitle());
            response.setStatus(app.getStatus().toString());
            response.setDescription(app.getJob().getDescription());
            response.setLocation(app.getJob().getLocation());
            response.setSkillsRequired(app.getJob().getSkillsRequired());
            response.setSalary(app.getJob().getSalary());
            response.setPostedAt(app.getJob().getPostedAt());
            response.setAppliedAt(app.getAppliedAt());
            return response;
        	
        }

    
    //for employer
    public List<ApplicantResponse> getApplicantsForJob(Long employerId, Long jobId) {
        // Validate job exists and belongs to employer
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("You are not authorized to view applicants for this job");
        }

        List<JobApplication> applications = jobApplicationRepository.findByJobId(jobId);

        return applications.stream().map(app -> {
            JobSeeker seeker = app.getJobSeeker();

            ApplicantResponse response = new ApplicantResponse();
            response.setApplicantId(seeker.getId());
            response.setName(seeker.getName());
            response.setEmail(seeker.getEmail());
          	response.setApplicationId(app.getId());
          	response.setAppliedDate(app.getAppliedAt());
          	response.setStatus(app.getStatus().toString());
          	
          	String filePath = seeker.getProfilePhoto();
          	if (filePath != null && !filePath.isEmpty()) {
          	    String normalizedPath = filePath.replace("\\", "/");
          	    response.setProfilePhoto(normalizedPath);
          	}

          	// Resume
          	String filePath1 = seeker.getResume();
          	if (filePath1 != null && !filePath1.isEmpty()) {
          	    String normalizedPath1 = filePath1.replace("\\", "/");
          	    response.setResume(normalizedPath1);
          	}
            return response;
        }).toList();
    }

	public void updateStatus(Long employerId, Long applicationId, String status) {
		
        
        Employer employer = employerRepository.findById(employerId)
        		.orElseThrow(() -> new RuntimeException("Employer not found"));

        JobApplication app= jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + applicationId));

            if (!app.getJob().getEmployer().getId().equals(employerId)) {
                throw new RuntimeException("You are not authorized to view this application.");
            }
            
            app.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));
            jobApplicationRepository.save(app);
		
	}
    
    
    
}

