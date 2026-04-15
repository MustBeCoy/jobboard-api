package com.jobboardapi.controller;

import com.jobboardapi.dto.JobRequest;
import com.jobboardapi.dto.JobResponse;
import com.jobboardapi.dto.PageResponse;
import com.jobboardapi.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // Only COMPANY can post jobs
    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<JobResponse> postJob(@Valid @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.postJob(request));
    }

    // Anyone logged in can see jobs
    @GetMapping
    public ResponseEntity<PageResponse<JobResponse>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "postedAt") String sortBy) {
        return ResponseEntity.ok(jobService.getAllJobs(page, size, sortBy));
    }

    // Anyone logged in can search
    @GetMapping("/search")
    public ResponseEntity<PageResponse<JobResponse>> searchJobs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.searchJobs(keyword, page, size));
    }

    // Anyone logged in can filter
    @GetMapping("/filter")
    public ResponseEntity<PageResponse<JobResponse>> filterJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String jobType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.filterJobs(location, jobType, page, size));
    }

    // Anyone logged in can see job by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    // Only COMPANY can see their own jobs
    @GetMapping("/my")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<JobResponse>> getMyJobs() {
        return ResponseEntity.ok(jobService.getMyJobs());
    }

    // COMPANY or ADMIN can delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMPANY', 'ADMIN')")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.deleteJob(id));
    }
}