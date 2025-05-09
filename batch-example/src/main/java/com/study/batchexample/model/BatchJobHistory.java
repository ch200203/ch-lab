package com.study.batchexample.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_job_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BatchJobHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobName;

    private String instanceId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private BatchJobStatus status;

    @Column(length = 500)
    private String message;

    private Integer rowsAffected;

    private String operatorId;

    private LocalDateTime createdAt = LocalDateTime.now();

    private BatchJobHistory(String jobName, String operatorId, String instanceId) {
        this.jobName = jobName;
        this.operatorId = operatorId;
        this.instanceId = instanceId;
        this.startTime = LocalDateTime.now();
        this.status = BatchJobStatus.RUNNING;
    }

    public static BatchJobHistory start(String jobName, String operatorId, String instanceId) {
        return new BatchJobHistory(jobName, operatorId, instanceId);
    }

    public void complete(Integer rowsAffected) {
        this.status = BatchJobStatus.SUCCESS;
        this.rowsAffected = rowsAffected;
        this.endTime = LocalDateTime.now();
    }


    public void fail(String message) {
        this.status = BatchJobStatus.FAIL;
        this.message = message;
        this.endTime = LocalDateTime.now();
    }

}

