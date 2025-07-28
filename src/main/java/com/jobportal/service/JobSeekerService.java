package com.jobportal.service;

import com.jobportal.config.CloudinaryConstants;
import com.jobportal.dto.JobSeekerRegisterRequest;
import com.jobportal.dto.JobSeekerResponse;
import com.jobportal.dto.JobSeekerUpdateRequest;
import com.jobportal.dto.UploadFileResponse;
import com.jobportal.model.JobSeeker;
import com.jobportal.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class JobSeekerService {

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CloudinaryService cloudinaryService;

    
    //register job seeker
    public void registerJobSeeker(JobSeekerRegisterRequest request) {
        // Check for duplicate email
        if (jobSeekerRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Job Seeker already exists with this email");
        }
        try {
	        JobSeeker jobSeeker = new JobSeeker();
	        jobSeeker.setName(request.getName());
	        jobSeeker.setEmail(request.getEmail());
	        jobSeeker.setPassword(passwordEncoder.encode(request.getPassword()));
	
        	// Save resume
        	if (request.getResume() != null && !request.getResume().isEmpty()) {
        		UploadFileResponse profileResponse = cloudinaryService.uploadFile(request.getResume(), CloudinaryConstants.JOBSEEKER_RESUMES_FOLDER);
        		jobSeeker.setResume(profileResponse.getUrl());
        		jobSeeker.setResumePublicId(profileResponse.getPublicId());
        	}
        	
        	if (request.getProfilePhoto() != null && !request.getProfilePhoto().isEmpty()) {
        		UploadFileResponse profileResponse = cloudinaryService.uploadFile(request.getProfilePhoto(), CloudinaryConstants.JOBSEEKER_PROFILES_FOLDER);
        		jobSeeker.setProfilePhoto(profileResponse.getUrl());
        		jobSeeker.setProfilePhotoPublicId(profileResponse.getPublicId());
        	}
	        	//save data in database 
	        	jobSeekerRepository.save(jobSeeker);
	        }
        catch (Exception e) {
        	throw new RuntimeException("test errror");
		}
    }

    public JobSeekerResponse loginJobSeeker(String email, String password) {
        JobSeeker jobSeeker = jobSeekerRepository.findByEmail(email);
        if (jobSeeker == null) {
            throw new RuntimeException("Job Seeker not found");
        }
        if (!passwordEncoder.matches(password, jobSeeker.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        JobSeekerResponse response = new JobSeekerResponse();
        response.setId(jobSeeker.getId());
        response.setName(jobSeeker.getName());
        response.setEmail(jobSeeker.getEmail());
//        response.setProfilePhoto(jobSeeker.getProfilePhoto());
//        response.setRole("JOB_SEEKER");
        
        return response;
    }
    
    
    //job seeker details (profile)
    public JobSeeker getJobSeekerById(Long id) {
        return jobSeekerRepository.findById(id).orElse(null);
    }
    
    
    //update profile
    public JobSeeker updateJobSeeker(Long id, JobSeekerUpdateRequest request) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Job seeker not found"));

        try {
            jobSeeker.setName(request.getName());

            // Update resume
            if (request.getResume() != null && !request.getResume().isEmpty()) {
                // Delete old resume from Cloudinary if exists
                if (jobSeeker.getResumePublicId() != null && !jobSeeker.getResumePublicId().isEmpty()) {
                    cloudinaryService.deleteFile(jobSeeker.getResumePublicId());
                }

                // Upload new resume
                UploadFileResponse resumeResponse = cloudinaryService.uploadFile(request.getResume(), CloudinaryConstants.JOBSEEKER_RESUMES_FOLDER);
                jobSeeker.setResume(resumeResponse.getUrl());
                jobSeeker.setResumePublicId(resumeResponse.getPublicId());
            }

            // Update profile photo
            if (request.getProfilePhoto() != null && !request.getProfilePhoto().isEmpty()) {
                // Delete old profile photo from Cloudinary if exists
                if (jobSeeker.getProfilePhotoPublicId() != null && !jobSeeker.getProfilePhotoPublicId().isEmpty()) {
                    cloudinaryService.deleteFile(jobSeeker.getProfilePhotoPublicId());
                }

                // Upload new profile photo
                UploadFileResponse photoResponse = cloudinaryService.uploadFile(request.getProfilePhoto(), CloudinaryConstants.JOBSEEKER_PROFILES_FOLDER);
                jobSeeker.setProfilePhoto(photoResponse.getUrl());
                jobSeeker.setProfilePhotoPublicId(photoResponse.getPublicId());
            }

            return jobSeekerRepository.save(jobSeeker);

        } catch (Exception e) {
            throw new RuntimeException("Updation Failed: " + e.getMessage());
        }
    }

    
    
//    reset password
    public void resetPassword(Long id, String oldPassword, String newPassword) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Job seeker not found."));

        if (!passwordEncoder.matches(oldPassword, jobSeeker.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }

        jobSeeker.setPassword(passwordEncoder.encode(newPassword));
        jobSeekerRepository.save(jobSeeker);
    }

}
