package com.study.eventlab

import com.study.eventlab.command.ClickCommand
import com.study.eventlab.common.redis.service.RedisService
import com.study.eventlab.config.logger
import com.study.eventlab.infrastructure.EventRepository
import com.study.eventlab.service.EventService
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class EventCounterPerformanceTest {
    companion object {
        private val log = logger()
    }

    @Autowired
    lateinit var eventCounterService: EventService

    @Autowired
    lateinit var repository: EventRepository

    @Autowired
    lateinit var redisService: RedisService

    private val eventId = "testEvent"
    private val totalClicks = 1000
    private val threadPoolSize = 50

    @BeforeEach
    fun setUp() {
        // DB, Redis 초기화
        repository.deleteAll()
        redisService.del("event:count:$eventId*")
    }

    @Test
    fun getRedisValue() {
        log.info("get : {}", redisService.get(eventId))
    }

    @Test
    fun `concurrent click performance test`() {
        val executor = Executors.newFixedThreadPool(threadPoolSize)
        val startLatch = CountDownLatch(1)
        val doneLatch = CountDownLatch(totalClicks)
        val timings = Collections.synchronizedList(mutableListOf<Long>())

        // 작업 스케줄링
        repeat(totalClicks) {
            executor.submit {
                startLatch.await()
                val startTime = System.nanoTime()
                val response = eventCounterService.click(
                    ClickCommand(
                        deviceId = "",
                        gender = "M",
                        age = 30,
                        ip = "127.0.0.1",
                        userAgent = "Spring-Test"
                    )
                )
                val elapsed = System.nanoTime() - startTime
                timings.add(elapsed)
                doneLatch.countDown()
            }
        }

        // 동시 시작
        val overallStart = System.currentTimeMillis()
        startLatch.countDown()
        doneLatch.await()
        val overallEnd = System.currentTimeMillis()
        executor.shutdown()

        // 결과 로그
        val durationMs = overallEnd - overallStart
        log.info("Total time for $totalClicks clicks: {} ms", durationMs)
        val avgNs = timings.average()
        log.info("Average latency per click: {} ms", avgNs / 1_000_000)

        // 검증
        val dbCount = repository.count()
        val redisCount = redisService.get("event:count:$eventId")!!.toLong()

        assertEquals(totalClicks.toLong(), dbCount, "DB should have recorded all clicks")
        assertEquals(totalClicks.toLong(), redisCount, "Redis counter should match total clicks")
    }
}
