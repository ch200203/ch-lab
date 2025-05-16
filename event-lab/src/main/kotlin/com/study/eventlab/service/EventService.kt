package com.study.eventlab.service

import com.study.eventlab.command.ClickCommand
import com.study.eventlab.common.redis.service.RedisService
import com.study.eventlab.config.logger
import com.study.eventlab.dto.ClickResponseDto
import org.springframework.stereotype.Service

@Service
class EventService(
    private val redisService: RedisService,
) {

    companion object {
        val log = logger()
    }

    /**
     * 클랙 했을때 Redis 에 count 적재시킨다.
     */
    fun click(
        clickCommand: ClickCommand,
    ): ClickResponseDto {
        log.info(clickCommand.toString() + clickCommand.eventId.toString())

        // redis 로 빼서 처리 필요.
        // 동시에 많은 클릭을 방지하기 위해서 어떤 장치를 취할 것인지 설계가 필요함 -> AOP 로 빼서 처리하면 좋을듯?
        val count = redisService.increment(clickCommand.cacheKey()) ?: 0

        return ClickResponseDto(
            message = "참여 감사합니다!",
            clickCount = count,
            totalCount = incrementTotalCount()
        )
    }

    /**
     * 이벤트를 클릭한 횟수를 redis 에 기록한다.
     * => redis 를 저장소로 사용하는건 좋지 않음
     * redis 와 동시에 Transaction 으로 DB 에 row 를 기록하자
     */
    private fun incrementTotalCount(): Long {
        return redisService.increment("event:total:count:") ?: 0
    }

    // Total Count 를 Redis에서 조회해서 가져옴
    fun getTotalCount(): Long {
        return redisService["event:total:count:"]?.toLong() ?: 0
    }

    // DB 저장과는 별개로 Redis 에서는 별개로 뭔가 해야한다.

}
