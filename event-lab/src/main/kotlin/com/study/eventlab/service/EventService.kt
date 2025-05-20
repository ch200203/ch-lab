package com.study.eventlab.service

import com.study.eventlab.command.ClickCommand
import com.study.eventlab.common.redis.service.RedisService
import com.study.eventlab.config.logger
import com.study.eventlab.dto.ClickResponseDto
import com.study.eventlab.infrastructure.EventRepository
import com.study.eventlab.model.EventModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.time.Duration.Companion.seconds

@Service
class EventService(
    private val redisService: RedisService,
    private val repository: EventRepository,
) {

    companion object {
        val log = logger()
        private const val KEY_PREFIX = "event:click:count:"

        // 1시간 TTL
        private val CACHE_TTL = 30.seconds
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
        // val eventKey = "$KEY_PREFIX:${eventId}"
        // redisService.setIfAbsent(eventKey, 0, CACHE_TTL)

        // 중복 참여 방지를 위한 개별 카운트 처리
        val clickCount = redisService.increment(clickCommand.cacheKey())

        log.info("clickCount={}, cacheKey={}", clickCommand.eventId, clickCommand.cacheKey())

        // 3) 전체 참여 카운트 처리
        val totalCount = computeTotalCount(eventId)

        return ClickResponseDto(
            message = "참여 감사합니다!",
            clickCount = clickCount ?: 0L,
            totalCount = totalCount
        )
    }

    private fun validateClickCount(clickCount: Int) {
        if (clickCount.toInt() >= 1) {
            throw IllegalArgumentException("단기간에 중복참여는 불가합니다")
        }
    }

    /**
     * 전체 참여 수를 캐싱.
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

    /**
     * 강제로 redis 값을 업데이트 시키는 메서드
     */
    fun getCurrentCount(eventId: Long): Long {
        // 키 생성 (다른 메서드와 동일한 패턴 사용)
        val key = "${KEY_PREFIX}:${eventId}"

        // 1) DB에서 현재 이벤트별 클릭 수 집계
        //    - eventId 기준으로 countByEventId() 를 구현해두면 편리합니다.
        val dbCount: Long = repository.count()

        // 2) Redis에 덮어쓰기 + TTL 갱신
        redisService.set(key, dbCount.toString(), CACHE_TTL)

        // 3) 동기화된 값을 반환
        return dbCount
    }


    /**
     * 삭제 테스트용 메서드
     * 나중에 제거해야함
     */
    fun del(keyPattern: String) {
        redisService.del("event:$keyPattern")
    }
}
