//package com.jobboardapi.service;
//
//import com.jobboardapi.dto.JobRequest;
//import com.jobboardapi.dto.JobResponse;
//import com.jobboardapi.entity.Job;
//import com.jobboardapi.entity.JobType;
//import com.jobboardapi.entity.User;
//import com.jobboardapi.repository.JobRepository;
//import com.jobboardapi.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class JobService {
//
//    private final JobRepository jobRepository;
//    private final UserRepository userRepository;
//
//    // Post a job
//    public JobResponse postJob(JobRequest request) {
//        // Get currently logged in user's email from token
//        String email = SecurityContextHolder.getContext()
//                .getAuthentication().getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found!"));
//
//        Job job = Job.builder()
//                .title(request.getTitle())
//                .description(request.getDescription())
//                .location(request.getLocation())
//                .salary(request.getSalary())
//                .companyName(request.getCompanyName())
//                .jobType(JobType.valueOf(request.getJobType().toUpperCase()))
//                .postedBy(user)
//                .build();
//
//        Job saved = jobRepository.save(job);
//        return mapToResponse(saved);
//    }
//
//    // Get all jobs
//    public List<JobResponse> getAllJobs() {
//        return jobRepository.findAll()
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    // Get job by ID
//    public JobResponse getJobById(Long id) {
//        Job job = jobRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Job not found!"));
//        return mapToResponse(job);
//    }
//
//    // Get jobs posted by logged in company
//    public List<JobResponse> getMyJobs() {
//        String email = SecurityContextHolder.getContext()
//                .getAuthentication().getName();
//        return jobRepository.findByPostedBy_Email(email)
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    // Delete job
//    public String deleteJob(Long id) {
//        jobRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Job not found!"));
//        jobRepository.deleteById(id);
//        return "Job deleted successfully!";
//    }
//
//    // Convert Job → JobResponse
//    private JobResponse mapToResponse(Job job) {
//        JobResponse response = new JobResponse();
//        response.setId(job.getId());
//        response.setTitle(job.getTitle());
//        response.setDescription(job.getDescription());
//        response.setLocation(job.getLocation());
//        response.setSalary(job.getSalary());
//        response.setCompanyName(job.getCompanyName());
//        response.setJobType(job.getJobType().name());
//        response.setPostedBy(job.getPostedBy().getName());
//        response.setPostedAt(job.getPostedAt());
//        return response;
//    }
//}
package com.jobboardapi.service;

import com.jobboardapi.dto.JobRequest;
import com.jobboardapi.dto.JobResponse;
import com.jobboardapi.dto.PageResponse;
import com.jobboardapi.entity.Job;
import com.jobboardapi.entity.JobType;
import com.jobboardapi.entity.User;
import com.jobboardapi.repository.JobRepository;
import com.jobboardapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        return mapToResponse(jobRepository.save(job));
    }

    // Get all jobs with pagination + sorting
    public PageResponse<JobResponse> getAllJobs(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Job> jobPage = jobRepository.findAll(pageable);
        return mapToPageResponse(jobPage);
    }

    // Search jobs by keyword
    public PageResponse<JobResponse> searchJobs(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());
        Page<Job> jobPage = jobRepository.searchByKeyword(keyword, pageable);
        return mapToPageResponse(jobPage);
    }

    // Filter jobs
    public PageResponse<JobResponse> filterJobs(String location,
                                                String jobType,
                                                int page,
                                                int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());
        Page<Job> jobPage;

        if (location != null && jobType != null) {
            jobPage = jobRepository.findByLocationAndJobType(
                    location, JobType.valueOf(jobType.toUpperCase()), pageable);
        } else if (location != null) {
            jobPage = jobRepository.findByLocation(location, pageable);
        } else if (jobType != null) {
            jobPage = jobRepository.findByJobType(
                    JobType.valueOf(jobType.toUpperCase()), pageable);
        } else {
            jobPage = jobRepository.findAll(pageable);
        }

        return mapToPageResponse(jobPage);
    }

    // Get job by ID
    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found!"));
        return mapToResponse(job);
    }

    // Get my jobs
    public List<JobResponse> getMyJobs() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return jobRepository.findAll()
                .stream()
                .filter(job -> job.getPostedBy().getEmail().equals(email))
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

    // Convert Page<Job> → PageResponse<JobResponse>
    private PageResponse<JobResponse> mapToPageResponse(Page<Job> page) {
        List<JobResponse> content = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
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