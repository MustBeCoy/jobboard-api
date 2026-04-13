package com.jobboardapi.repository;

import com.jobboardapi.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface JobRepository extends JpaRepository<Job,Long> {

    List<Job> findByPostedBy_Email(String email);

    List<Job> findByLocation(String location);

    List<Job> findByJobType(String jobType);
}
