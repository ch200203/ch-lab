package com.study.batchexample.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class SampleService {

    public void someService() {
        log.info("특정 서비스 실행");
        // throw new IllegalArgumentException("특정 오류 발생");
    }
}
