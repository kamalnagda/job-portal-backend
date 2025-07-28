package com.jobportal.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jobportal.config.CloudinaryConstants;
import com.jobportal.dto.EmployerRegisterRequest;
import com.jobportal.dto.EmployerUpdateRequest;
import com.jobportal.dto.UploadFileResponse;
import com.jobportal.model.Employer;
import com.jobportal.repository.EmployerRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployerService {

    @Autowired
    private EmployerRepository employerRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CloudinaryService cloudinaryService;

    //registration method
    public Employer registerEmployer(EmployerRegisterRequest request) {
        // Save company logo if provided
        
        if (employerRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("employer already exists with this email");
        }
        
        Employer employer = new Employer();
        if (request.getCompanyLogo() != null && !request.getCompanyLogo().isEmpty()) {
    		UploadFileResponse profileResponse = cloudinaryService.uploadFile(request.getCompanyLogo(), CloudinaryConstants.EMPLOYER_LOGOS_FOLDER);
    		employer.setCompanyLogo(profileResponse.getUrl());
    		employer.setCompanyLogoPublicId(profileResponse.getPublicId());
        }
        employer.setName(request.getName());
        employer.setEmail(request.getEmail());
        employer.setPassword(passwordEncoder.encode(request.getPassword())); 
        employer.setCompanyName(request.getCompanyName());

        return employerRepository.save(employer);
    }
    
    
    public Employer loginEmployer(String email, String password) {
        Employer employer = employerRepository.findByEmail(email);
        if (employer == null) {
            throw new RuntimeException("employer not found");
        }
        if (!passwordEncoder.matches(password, employer.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return employer;
    }

    
    //view profile
	public Employer getEmployerById(Long id) {
		return employerRepository.findById(id).orElse(null);
	}
	
	//	update profile
	public Employer updateEmployer(Long id, EmployerUpdateRequest request) {
		
			Employer employer = employerRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Employer not found"));
			
			if (request.getCompanyLogo() != null && !request.getCompanyLogo().isEmpty()) {
		        if (employer.getCompanyLogoPublicId() != null && !employer.getCompanyLogoPublicId().isEmpty()) {
		            cloudinaryService.deleteFile(employer.getCompanyLogoPublicId());
		        }
	    		UploadFileResponse profileResponse = cloudinaryService.uploadFile(request.getCompanyLogo(), CloudinaryConstants.EMPLOYER_LOGOS_FOLDER);
	    		employer.setCompanyLogo(profileResponse.getUrl());
	    		employer.setCompanyLogoPublicId(profileResponse.getPublicId());
			}
			employer.setName(request.getName());
			employer.setCompanyName(request.getCompanyName());
			
			
			return employerRepository.save(employer);
	}
	
//	reset password
	public void resetPassword(Long id, String oldPassword, String newPassword) {
	    Employer employer = employerRepository.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("Employer not found"));

	    if (!passwordEncoder.matches(oldPassword, employer.getPassword())) {
	        throw new IllegalArgumentException("Old password is incorrect.");
	    }

	    employer.setPassword(passwordEncoder.encode(newPassword));
	    employerRepository.save(employer);
	}
}

