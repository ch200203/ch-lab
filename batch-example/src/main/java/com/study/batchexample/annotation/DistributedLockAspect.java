package com.study.batchexample.annotation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(lock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock lock) throws Throwable {
        RLock rLock = redissonClient.getLock(lock.key());
        boolean isLocked = false;

        try {
            isLocked = rLock.tryLock(0, lock.timeoutSeconds(), TimeUnit.SECONDS);

            log.info("<UNK>lock<UNK>{}", isLocked);

            if (!isLocked) {
                // 선점 실패
                return 0;
            }

            return joinPoint.proceed();

        } finally {
            if (isLocked) {
                rLock.unlock();
            }
        }
    }

}
