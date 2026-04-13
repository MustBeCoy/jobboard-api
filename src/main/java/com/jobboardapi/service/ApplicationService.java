package com.jobboardapi.service;

import com.jobboardapi.dto.ApplicationRequest;
import com.jobboardapi.dto.ApplicationResponse;
import com.jobboardapi.entity.Application;
import com.jobboardapi.entity.Job;
import com.jobboardapi.entity.User;
import com.jobboardapi.exception.AlreadyExistsException;
import com.jobboardapi.exception.ResourceNotFoundException;
import com.jobboardapi.repository.ApplicationRepository;
import com.jobboardapi.repository.JobRepository;
import com.jobboardapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    // Apply for a job
    public ApplicationResponse applyForJob(ApplicationRequest request) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User applicant = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found!"));

        if (applicationRepository.existsByJob_IdAndApplicant_Email(
                request.getJobId(), email)) {
            throw new AlreadyExistsException("You already applied for this job!");
        }

        Application application = Application.builder()
                .job(job)
                .applicant(applicant)
                .coverLetter(request.getCoverLetter())
                .build();

        Application saved = applicationRepository.save(application);
        return mapToResponse(saved);
    }

    // Get my applications (USER)
    public List<ApplicationResponse> getMyApplications() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return applicationRepository.findByApplicant_Email(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get all applications for a job (COMPANY)
    public List<ApplicationResponse> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJob_Id(jobId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update application status (COMPANY)
    public ApplicationResponse updateStatus(Long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found!"));
        application.setStatus(
                com.jobboardapi.entity.ApplicationStatus.valueOf(status.toUpperCase())
        );

        Application updated = applicationRepository.save(application);
        return mapToResponse(updated);
    }

    // Convert Application → ApplicationResponse
    private ApplicationResponse mapToResponse(Application app) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(app.getId());
        response.setJobTitle(app.getJob().getTitle());
        response.setCompanyName(app.getJob().getCompanyName());
        response.setApplicantName(app.getApplicant().getName());
        response.setApplicantEmail(app.getApplicant().getEmail());
        response.setStatus(app.getStatus().name());
        response.setCoverLetter(app.getCoverLetter());
        response.setAppliedAt(app.getAppliedAt());
        return response;
    }
}