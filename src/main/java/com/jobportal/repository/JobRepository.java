package com.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.model.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByEmployerId(Long employerId);
    
    List<Job> findByTitleContainingIgnoreCase(String title);

    List<Job> findByLocationIgnoreCase(String location);

    List<Job> findBySkillsRequiredContainingIgnoreCase(String skills);

    List<Job> findByTitleContainingIgnoreCaseAndLocationIgnoreCase(
        String title, String location);

    List<Job> findByTitleContainingIgnoreCaseAndSkillsRequiredContainingIgnoreCase(
        String title, String skills);

    List<Job> findByLocationIgnoreCaseAndSkillsRequiredContainingIgnoreCase(
        String location, String skills);

    List<Job> findByTitleContainingIgnoreCaseAndLocationIgnoreCaseAndSkillsRequiredContainingIgnoreCase(
        String title, String location, String skills);

}
