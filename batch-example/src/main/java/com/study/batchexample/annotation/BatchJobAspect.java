package com.study.batchexample.annotation;

import com.study.batchexample.model.BatchJobHistory;
import com.study.batchexample.service.BatchJobLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BatchJobAspect {

    private final BatchJobLogger batchJobLogger;

    @PersistenceContext
    private final EntityManager em;

    @Around("@annotation(batchJob)")
    public Object around(ProceedingJoinPoint joinPoint, BatchJob batchJob) throws Throwable {
        BatchJobHistory history = batchJobLogger.start(batchJob.name(), batchJob.operatorId());
        log.info("▶ [A 트랜잭션 직후] isPersistent = {}", em.contains(history));

        try {
            Object result = joinPoint.proceed(); // 실제 메소드 실행
            log.info("▶ [complete 호출 전] isPersistent in main = {}", em.contains(history));

            batchJobLogger.complete(history, (Integer) result);

            log.info("▶ [complete 호출 후] isPersistent in main = {}", em.contains(history)); //
            return result;

        } catch (Exception e) {
            log.info("▶ [fail 호출 전] isPersistent in main = {}", em.contains(history)); //
            // 예외를 잡아서 로그에 저장
            batchJobLogger.fail(history, "FAIL");

            throw e;
        }
    }
}
