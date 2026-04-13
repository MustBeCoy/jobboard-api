package com.jobboardapi.controller;

import com.jobboardapi.dto.JobRequest;
import com.jobboardapi.dto.JobResponse;
import com.jobboardapi.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // POST /api/jobs — post a job (COMPANY only)
    @PostMapping
    public ResponseEntity<JobResponse> postJob(@RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.postJob(request));
    }

    // GET /api/jobs — get all jobs (anyone logged in)
    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    // GET /api/jobs/1 — get single job by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    // GET /api/jobs/my — get jobs posted by logged in company
    @GetMapping("/my")
    public ResponseEntity<List<JobResponse>> getMyJobs() {
        return ResponseEntity.ok(jobService.getMyJobs());
    }

    // DELETE /api/jobs/1 — delete a job
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.deleteJob(id));
    }
}