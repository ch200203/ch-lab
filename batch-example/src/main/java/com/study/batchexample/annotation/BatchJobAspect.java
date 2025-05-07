package com.study.batchexample.annotation;

import com.study.batchexample.model.BatchJobHistory;
import com.study.batchexample.service.BatchJobLogger;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class BatchJobAspect {

    private final BatchJobLogger batchJobLogger;

    @Around("@annotation(batchJob)")
    public Object around(ProceedingJoinPoint joinPoint, BatchJob batchJob) throws Throwable {
        BatchJobHistory history = batchJobLogger.start(batchJob.name(), batchJob.operatorId());

        try {
            Object result = joinPoint.proceed(); // 실제 메소드 실행
            batchJobLogger.complete(history, (Integer) result);
            return result;

        } catch (Exception e) {
            batchJobLogger.fail(history, "FAIL");
            throw e;
        }
    }
}
