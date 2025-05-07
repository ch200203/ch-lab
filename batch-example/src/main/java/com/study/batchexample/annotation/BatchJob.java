package com.study.batchexample.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BatchJob {
    /**
     * 배치 이름
     */
    String name();

    /**
     * 실행자
     */
    String operatorId() default "system";
}
