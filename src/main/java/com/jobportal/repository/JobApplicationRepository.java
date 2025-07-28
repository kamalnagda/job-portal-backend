package com.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.model.JobSeeker;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByJobSeekerAndJob(JobSeeker jobSeeker, Job job);
    List<JobApplication> findByJobSeekerId(Long jobSeekerId);
    List<JobApplication> findByJobId(Long jobId);
}
