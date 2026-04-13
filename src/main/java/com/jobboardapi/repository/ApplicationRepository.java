package com.jobboardapi.repository;

import com.jobboardapi.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // All applications for a specific job
    List<Application> findByJob_Id(Long jobId);

    // All applications by a specific user
    List<Application> findByApplicant_Email(String email);

    // Check if user already applied
    boolean existsByJob_IdAndApplicant_Email(Long jobId, String email);
}