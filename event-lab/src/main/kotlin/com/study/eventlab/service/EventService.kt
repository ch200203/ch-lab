package com.study.eventlab.service

import com.study.eventlab.command.ClickCommand
import com.study.eventlab.common.redis.service.RedisService
import com.study.eventlab.config.logger
import com.study.eventlab.dto.ClickResponseDto
import com.study.eventlab.infrastructure.EventRepository
import com.study.eventlab.model.EventModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
class EventService(
    private val redisService: RedisService,
    private val repository: EventRepository,
) {

    companion object {
        val log = logger()
        private const val KEY_PREFIX = "event:click:count:"

        // 1시간 TTL
        private val CACHE_TTL = Duration.ofHours(1)
    }

    /**
     * 사용자가 클릭할 때마다 호출되는 메서드
     * 1) DB에 클릭 기록 저장
     * 2) 해당 이벤트의 클릭 카운트 Redis INCR
     * 3) 전체 참여 카운트 조회/증가
     */
    @Transactional
    fun click(clickCommand: ClickCommand, eventId: Long): ClickResponseDto {
        log.info("clickCommand={}, eventId={}", clickCommand, clickCommand.eventId)

        // 1) DB 저장
        val model = EventModel.from(clickCommand)
        repository.save(model)

        // 2) 이벤트별 클릭 카운트 증가
        val eventKey = "$KEY_PREFIX:$eventId"

        // 키가 없으면 0 으로 초기화 후 TTL 설정 이거는 개별 카운트로 중복 참여를 막기 위함
        redisService.setIfAbsent(eventKey, 0, CACHE_TTL)
        val clickCount = redisService.increment(clickCommand.cacheKey())!!

        // 3) 전체 참여 카운트 처리
        val totalCount = computeTotalCount(eventId)

        return ClickResponseDto(
            message = "참여 감사합니다!",
            clickCount = clickCount,
            totalCount = totalCount
        )
    }

    /**
     * 전체 참여 수를 1시간 단위로 캐싱.
     * - 키가 없으면 DB에서 COUNT(*) 로 초기화하고 TTL 설정, 그 값을 반환
     * - 키가 있으면 INCR 로 1 증가시켜 반환
     */
    private fun computeTotalCount(eventId: Long): Long {
        val totalKey = "${KEY_PREFIX}:${eventId}"

        val existing = redisService.get(totalKey)
        log.info("Total Key is Existing : {}", existing)

        return if (existing != null) {
            // 이미 초기화된 경우, 원자적 증가
            redisService.increment(totalKey)!!
            redisService.get(totalKey)?.toLong() ?: repository.count()

        } else {
            // 캐시 미스 시 한 번만 DB 집계 → 초기화
            val count = repository.count()
            redisService.setIfAbsent(totalKey, count.toInt(), CACHE_TTL)
            count // 리턴 값
        }
    }

    /**
     * 1시간 단위로만 DB에서 COUNT(*) 를 호출하도록
     * Redis TTL 을 1시간으로 설정.
     */
    fun getTotalCount(eventId: String): Long {
        val key = "${KEY_PREFIX}:${eventId}"

        // 1) 캐시가 있으면 바로 반환
        redisService[key]?.let { return it.toLong() }

        // 2) 캐시가 없으면 DB에서 집계 → Redis에 SETNX+TTL
        val count = repository.count()

        // Redis에 키가 없을 때만 쓰면서 TTL 설정
        redisService.setIfAbsent(key, count.toInt(), CACHE_TTL)

        return count
    }

    fun del(keyPattern: String) {
        redisService.del("event:$keyPattern")
    }
}
