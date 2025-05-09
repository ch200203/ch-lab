package com.study.batchexample.schedule;

import com.study.batchexample.annotation.BatchJob;
import com.study.batchexample.annotation.DistributedLock;
import com.study.batchexample.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class ScheduleDeleteTask {

    private final RedissonClient redissonClient;
    private final SampleService sampleService;

    @Scheduled(cron = "0 */5 * * * *")
    @BatchJob(name = "deleteData", operatorId = "system")
    @DistributedLock(key = "delete-batch-lock")
    public int deleteData() {

        sampleService.someService();

        return new Random().nextInt(100);
    }
}
