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
        logTx(Thread.currentThread().getStackTrace()[1].getMethodName());
        BatchJobHistory history = BatchJobHistory.start(jobName, operatorId, getHostName());
        return batchJobRepository.save(history);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void complete(BatchJobHistory history, Integer rowsAffected) {
        log.info(">>> before merge: isPersistent = {}", em.contains(history)); // false 예상
        logTx(Thread.currentThread().getStackTrace()[1].getMethodName());
        history.complete(rowsAffected);
        // batchJobRepository.save(history);
        log.info(">>> after merge: isPersistent = {}", em.contains(history)); // 여전히 false (save는 merge된 객체를
    }

    /**
     * 실패 처리 메서드
     *
     * @param history
     * @param message 실패원인 기록
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void fail(BatchJobHistory history, String message) {
        log.info(">>> FAIL 호출됨");
        log.info("isNewTransaction? {}", TransactionSynchronizationManager.isActualTransactionActive());
        log.info(">>> before merge: isPersistent = {}", em.contains(history)); // false 예상

        logTx(Thread.currentThread().getStackTrace()[1].getMethodName());

        history.fail(message); // 필드 변경 -> 준영속?
        // batchJobRepository.save(history);

        // batchJobRepository.saveAndFlush(history); // 테스트 용으로 Flush 이거는 동작함
        log.info(">>> FAIL 저장됨");
        log.info(">>> after merge: isPersistent = {}", em.contains(history)); // 여전히 false (save는 merge된 객체를 반환함)
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
