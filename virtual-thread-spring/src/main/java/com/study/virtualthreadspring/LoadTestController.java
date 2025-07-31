package com.study.virtualthreadspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/load")
public class LoadTestController {

    private static final Logger log = LoggerFactory.getLogger(LoadTestController.class);

    @GetMapping
    public String load() throws InterruptedException {
        log.info("do Something");
        Thread.sleep(1000);
        return Thread.currentThread().getName();
    }
}
