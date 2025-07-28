package com.jobportal.model;

import java.time.LocalDateTime;

import com.jobportal.model.ApplicationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private JobSeeker jobSeeker;

    @ManyToOne
    private Job job;

    private LocalDateTime appliedAt;
    
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    public JobApplication(JobSeeker jobSeeker, Job job) {
        this.jobSeeker = jobSeeker;
        this.job = job;
        this.appliedAt = LocalDateTime.now();
    }
}


