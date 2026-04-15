package com.jobboardapi.controller;

import com.jobboardapi.dto.ApplicationRequest;
import com.jobboardapi.dto.ApplicationResponse;
import com.jobboardapi.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // Only USER can apply
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApplicationResponse> applyForJob(
            @RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.applyForJob(request));
    }

    // Only USER can see their applications
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        return ResponseEntity.ok(applicationService.getMyApplications());
    }

    // Only COMPANY can see applications for their job
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForJob(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsForJob(jobId));
    }

    // Only COMPANY can update application status
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status));
    }
}