package com.lasyankali.erp.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.lasyankali.erp.entity.enums.BatchLevel;
import com.lasyankali.erp.entity.enums.BatchStatus;
import com.lasyankali.erp.entity.enums.Discipline;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Batch {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "batch_code", nullable = false, unique = true)
    private String batchCode;

    @Column(name = "batch_name", nullable = false)
    private String batchName;

    @Enumerated(EnumType.STRING)
    @Column(name = "discipline", nullable = false)
    private Discipline discipline;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private BatchLevel level;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "capacity")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BatchStatus status;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
