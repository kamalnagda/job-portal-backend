package com.jobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jobportal.model.Admin;
import com.jobportal.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    


    public Admin loginAdmin(String email, String password) {

        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new RuntimeException("Admin not found");
        }
        if (!password.equals(admin.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }
        

        return admin;
    }
}
