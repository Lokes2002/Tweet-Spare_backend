package com.baap.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class AssignmentSubmission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Assignment assignment;

    @ManyToOne
    private User user;  // This can be null for unauthenticated submissions

    private String submissionContent;

    // Constructor, getters, setters, etc.
    public AssignmentSubmission() {}

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSubmissionContent(String submissionContent) {
        this.submissionContent = submissionContent;
    }
}
