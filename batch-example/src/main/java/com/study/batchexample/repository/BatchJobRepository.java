package com.study.batchexample.repository;

import com.study.batchexample.model.BatchJobHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchJobRepository extends JpaRepository<BatchJobHistory, Long> {
}
