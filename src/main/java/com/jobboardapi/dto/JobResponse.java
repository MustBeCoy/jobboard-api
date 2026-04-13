package com.jobboardapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {
    private Long id ;
    private String title ;
    private String description ;
    private String location;
    private String salary ;
    private String companyName ;
    private String jobType ;
    private String postedBy;
    private LocalDateTime postedAt;


}
