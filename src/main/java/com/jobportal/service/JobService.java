package com.jobportal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobportal.dto.JobPostRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.dto.JobUpdateRequest;
import com.jobportal.model.Employer;
import com.jobportal.model.Job;
import com.jobportal.repository.EmployerRepository;
import com.jobportal.repository.JobRepository;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployerRepository employerRepository;

    public Job postJob(Long employerId, JobPostRequest request) {
    	
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSkillsRequired(request.getSkillsRequired());
        job.setSalary(request.getSalary());
        job.setEmployer(employer);
        job.setPostedAt(LocalDateTime.now());

        return jobRepository.save(job);
    }
    
    public Job updateJob(Long employerId, Long jobId, JobUpdateRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        if (!job.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("You are not authorized to update this job");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSkillsRequired(request.getSkillsRequired());
        job.setSalary(request.getSalary());

        return jobRepository.save(job);
    }
    
    public void deleteJob(Long employerId, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        if (!job.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("You are not authorized to delete this job");
        }

        jobRepository.delete(job);
    }
    
    //for employer only
    public List<Job> getJobsByEmployer(Long employerId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        return jobRepository.findByEmployerId(employerId);
    }
    
    //for job seeker
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    
    //search job filter job 
    public List<Job> searchJobs(String title, String location, String skills) {
    	
    	
        if (title != null && title.trim().isEmpty()) title = null;
        if (location != null && location.trim().isEmpty()) location = null;
        if (skills != null && skills.trim().isEmpty()) skills = null;
    	
    	
    	
        if (title != null && location != null && skills != null) {
            return jobRepository.findByTitleContainingIgnoreCaseAndLocationIgnoreCaseAndSkillsRequiredContainingIgnoreCase(
                title, location, skills);
        } else if (title != null && location != null) {
            return jobRepository.findByTitleContainingIgnoreCaseAndLocationIgnoreCase(title, location);
        } else if (title != null && skills != null) {
            return jobRepository.findByTitleContainingIgnoreCaseAndSkillsRequiredContainingIgnoreCase(title, skills);
        } else if (location != null && skills != null) {
            return jobRepository.findByLocationIgnoreCaseAndSkillsRequiredContainingIgnoreCase(location, skills);
        } else if (title != null) {
            return jobRepository.findByTitleContainingIgnoreCase(title);
        } else if (location != null) {
            return jobRepository.findByLocationIgnoreCase(location);
        } else if (skills != null) {
            return jobRepository.findBySkillsRequiredContainingIgnoreCase(skills);
        } else {
        	return jobRepository.findAll();
        }
    }
    
    public Job getJobById(Long id) {
    	
	    Optional<Job> job =	jobRepository.findById(id);
	    
	    if(job.isPresent())
	    {
	    	return job.get();
	    }
	    else
	    {
	    	throw new RuntimeException("no job found with this id");
	    }
    
    }
    
    public Job getJobById(long employerId,long jobId)
    {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
    	
    	
	    Optional<Job> job =	jobRepository.findById(jobId);
	    
	    if(job.isPresent())
	    {	
	    	Job jobData = job.get();
	    	
	    	if(jobData.getEmployer().getId().equals(employerId))
	    	{
	    		return jobData;
	    	}
	    	else
	    	{
	    		throw new RuntimeException("You are not authorized to delete this job");
	    	}
	    	
	    }
	    else
	    {
	    	throw new RuntimeException("no job found with this id");
	    }
    	
    	
    }
    
    
}
