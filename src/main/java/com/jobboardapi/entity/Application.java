package com.jobboardapi.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false )
    private Job job;
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn( name = "applicant_id", nullable = false )
    private User applicant ;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(length = 1000)
    private String coverLetter;

    private LocalDateTime appliedAt;

    @PrePersist
    public void prePersist() {
        this.appliedAt = LocalDateTime.now();
        this.status = ApplicationStatus.PENDING;
    }
}
