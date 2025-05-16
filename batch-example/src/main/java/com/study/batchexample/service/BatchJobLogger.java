package com.study.batchexample.service;

import com.study.batchexample.model.BatchJobHistory;
import com.study.batchexample.repository.BatchJobRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.net.InetAddress;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchJobLogger {

    private final BatchJobRepository batchJobRepository;

    @PersistenceContext
    private EntityManager em;

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
        log.info(">>> before merge: isPersistent = {}", em.contains(history));
        history.complete(rowsAffected);
    }

    /**
     * 실패 처리 메서드
     *
     * @param history
     * @param message 실패원인 기록
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void fail(BatchJobHistory history, String message) {
        log.info(">>> before merge: isPersistent = {}", em.contains(history));
        history.fail(message);
        log.info(">>> after merge: isPersistent = {}", em.contains(history)); // 여전히 false
        // batchJobRepository.save(history);
    }

    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }

    // 실제 트랜잭션이 분리되었는지 확인하기 위한 메서드
    private void logTx(String label) {
        log.info("[{}] tx-active={}, tx-name={}, thread={}",
                label,
                TransactionSynchronizationManager.isActualTransactionActive(),
                TransactionSynchronizationManager.getCurrentTransactionName(),
                Thread.currentThread().getName()
        );
    }

    private void checkIfPersistent(BatchJobHistory history) {
        boolean isPersistent = em.contains(history);
        log.info(">>> history isPersistent? {}", isPersistent);
    }
}
