package com.baap.controller;

import com.baap.model.Assignment;
import com.baap.model.AssignmentSubmission;
import com.baap.repository.AssignmentRepository;
import com.baap.repository.AssignmentSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    // POST endpoint to handle assignment submissions
    @PostMapping("/submit/{assignmentId}")
    public ResponseEntity<?> submitAssignment(@PathVariable Long assignmentId, 
                                              @RequestBody SubmissionRequest submissionRequest) {
        // Fetch the assignment based on ID
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        // Create a new AssignmentSubmission
        AssignmentSubmission assignmentSubmission = new AssignmentSubmission();
        
        // Set the assignment for this submission
        assignmentSubmission.setAssignment(assignment);

        // Set the submission content (the text submitted)
        assignmentSubmission.setSubmissionContent(submissionRequest.getSubmissionContent());

        // Save the assignment submission without a user (no user context)
        assignmentSubmissionRepository.save(assignmentSubmission);

        // Notify all users about the new submission (optional)
        // You can add logic here if needed (e.g., email or notification logic).

        return ResponseEntity.ok("Assignment submitted successfully!");
    }
}

// Submission request object
class SubmissionRequest {
    private String submissionContent;

    public String getSubmissionContent() {
        return submissionContent;
    }

    public void setSubmissionContent(String submissionContent) {
        this.submissionContent = submissionContent;
    }
}
