package com.study.virtualthreadspringv2.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseVirtualThread {
}

@Aspect
@Component
class VirtualThreadAspect {

    private final ExecutorService vtExecutor;

    public VirtualThreadAspect(ExecutorService vtExecutor) {
        this.vtExecutor = vtExecutor;
    }

    @Around("@annotation(UseVirtualThread)")
    public Object runOnVirtualThread(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {
                        try {
                            return joinPoint.proceed();
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    },
                    vtExecutor
            ).get();
        } catch (ExecutionException e) {
            // unpack 에서 발생하는 예외 던지기
            Throwable cause = e.getCause();
            if (cause != null) {
                throw cause;
            }
            throw e;
        }
    }
}
