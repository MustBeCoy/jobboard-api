package com.jobboardapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {
    private long id ;
    private String applicantName ;
    private String jobTitle ;
    private String companyName ;
    private String applicantEmail ;
    private String status ;
    private String coverLetter ;
    private LocalDateTime appliedAt ;

}
