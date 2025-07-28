package com.jobportal.repository;

import com.jobportal.model.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
    JobSeeker findByEmail(String email);
}

