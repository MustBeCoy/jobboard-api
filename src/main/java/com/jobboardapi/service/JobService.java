package com.jobboardapi.service;

import com.jobboardapi.dto.JobRequest;
import com.jobboardapi.dto.JobResponse;
import com.jobboardapi.entity.Job;
import com.jobboardapi.entity.JobType;
import com.jobboardapi.entity.User;
import com.jobboardapi.repository.JobRepository;
import com.jobboardapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    // Post a job
    public JobResponse postJob(JobRequest request) {
        // Get currently logged in user's email from token
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .salary(request.getSalary())
                .companyName(request.getCompanyName())
                .jobType(JobType.valueOf(request.getJobType().toUpperCase()))
                .postedBy(user)
                .build();

        Job saved = jobRepository.save(job);
        return mapToResponse(saved);
    }

    // Get all jobs
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get job by ID
    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found!"));
        return mapToResponse(job);
    }

    // Get jobs posted by logged in company
    public List<JobResponse> getMyJobs() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return jobRepository.findByPostedBy_Email(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Delete job
    public String deleteJob(Long id) {
        jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found!"));
        jobRepository.deleteById(id);
        return "Job deleted successfully!";
    }

    // Convert Job → JobResponse
    private JobResponse mapToResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setLocation(job.getLocation());
        response.setSalary(job.getSalary());
        response.setCompanyName(job.getCompanyName());
        response.setJobType(job.getJobType().name());
        response.setPostedBy(job.getPostedBy().getName());
        response.setPostedAt(job.getPostedAt());
        return response;
    }
}