//package com.jobboardapi.repository;
//
//import com.jobboardapi.entity.Job;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//@Repository
//public interface JobRepository extends JpaRepository<Job,Long> {
//
//    List<Job> findByPostedBy_Email(String email);
//
//    List<Job> findByLocation(String location);
//
//    List<Job> findByJobType(String jobType);
//}

package com.jobboardapi.repository;

import com.jobboardapi.entity.Job;
import com.jobboardapi.entity.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // Search by keyword in title or description
    @Query("SELECT j FROM Job j WHERE " +
            "LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Job> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Filter by location
    Page<Job> findByLocation(String location, Pageable pageable);

    // Filter by job type
    Page<Job> findByJobType(JobType jobType, Pageable pageable);

    // Filter by location AND job type
    Page<Job> findByLocationAndJobType(String location, JobType jobType, Pageable pageable);

    // Get all with pagination
    Page<Job> findAll(Pageable pageable);
}