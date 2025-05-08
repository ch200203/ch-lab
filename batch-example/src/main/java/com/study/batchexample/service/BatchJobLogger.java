package com.study.batchexample.service;

import com.study.batchexample.model.BatchJobHistory;
import com.study.batchexample.repository.BatchJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;

@Service
@RequiredArgsConstructor
public class BatchJobLogger {

    private final BatchJobRepository batchJobRepository;

    /**
     * 배치 실행 기록을 남기는 메서드
     *
     * @param jobName    job(작업) 이름
     * @param operatorId 작업자(실행자) Id
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BatchJobHistory start(String jobName, String operatorId) {
        BatchJobHistory history = BatchJobHistory.start(jobName, operatorId, getHostName());
        return batchJobRepository.save(history);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void complete(BatchJobHistory history, Integer rowsAffected) {
        history.complete(rowsAffected);
        // batchJobRepository.save(history);
    }

    /**
     * 실패 처리 메서드
     * @param history
     * @param message 실패원인 기록
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void fail(BatchJobHistory history, String message) {
        history.fail(message);
        // batchJobRepository.save(history);
    }

    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
