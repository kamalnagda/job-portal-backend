package com.jobportal.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String profilePhoto; // File path or URL
    
    private String profilePhotoPublicId;

    private String resume;       // File path or URL
    
    private String resumePublicId;
}