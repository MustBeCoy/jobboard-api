package com.jobboardapi.controller;

import com.jobboardapi.dto.ApplicationRequest;
import com.jobboardapi.dto.ApplicationResponse;
import com.jobboardapi.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // POST /api/applications — apply for job (USER)
    @PostMapping
    public ResponseEntity<ApplicationResponse> applyForJob(
            @RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.applyForJob(request));
    }

    // GET /api/applications/my — get my applications (USER)
    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        return ResponseEntity.ok(applicationService.getMyApplications());
    }

    // GET /api/applications/job/1 — get applications for a job (COMPANY)
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForJob(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsForJob(jobId));
    }

    // PUT /api/applications/1/status?status=ACCEPTED — update status (COMPANY)
    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status));
    }
}