package com.study.batchexample.schedule;

import com.study.batchexample.annotation.BatchJob;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ScheduleDeleteTask {

    private final RedissonClient redissonClient;

    @Scheduled(cron = "0 */5 * * * *")
    @BatchJob(name = "deleteData", operatorId = "system")
    public int deleteData() {
        RLock lock = redissonClient.getLock("delete-batch-lock");

        boolean isLocked = false;

        try {
            isLocked = lock.tryLock(0, 10, TimeUnit.SECONDS);

            if (isLocked) {

                // 삭제 메서드가 호출되어야함. 지금은 그냥 랜덤 숫자 리턴)
                return new Random().nextInt(100);

            } else {
                return 0; // 선점 실패 -> 0 처리, 없을 수도 있으니까 -1 or null 뭘로하지...
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0;
        } finally {
            if (isLocked) {
                lock.unlock();
            }
        }
    }
}
