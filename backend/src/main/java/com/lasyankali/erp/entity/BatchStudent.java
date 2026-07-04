package com.lasyankali.erp.entity;

import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.lasyankali.erp.entity.enums.EnrollmentStatus;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "batch_students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchStudent {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_student_id")
    private Long batchStudentId;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnrollmentStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
