package com.jobboardapi.dto;

import lombok.Data;

@Data
public class JobRequest {
    private String title;
    private String description;
    private String location;
    private String salary;
    private String companyName;
    private String jobType; // "FULL_TIME", "PART_TIME", "REMOTE", "INTERNSHIP"
}