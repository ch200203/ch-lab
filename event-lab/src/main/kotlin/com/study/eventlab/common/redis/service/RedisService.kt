package com.study.eventlab.common.redis.service

import com.study.eventlab.config.logger
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.toJavaDuration

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    companion object {
        val log = logger()
        private val DEFAULT_TTL = 1.hours// 기본 TTL 1시간
    }

    fun keys(keyPattern: String): Set<String> = redisTemplate.keys(keyPattern)

    operator fun get(key: String): String? = redisTemplate.opsForValue()[key]

    fun del(vararg patterns: String): Set<String> {
        val removedKeys = HashSet<String>()

        for (pattern in patterns) {
            removedKeys.addAll(del(pattern))
        }

        return removedKeys
    }

    fun del(pattern: String): Set<String> {
        val keys = keys(pattern)
        log.info("remove redis cache \"${pattern}\" ->  $keys")
        redisTemplate.delete(keys)

        return keys
    }

    operator fun set(key: String, value: String) {
        set(key, value, DEFAULT_TTL)
    }

    fun set(key: String, value: String, ttl: Duration) {
        redisTemplate.opsForValue().set(key, value, ttl.toJavaDuration())
    }

    fun increment(key: String): Long? {
        return redisTemplate.opsForValue().increment(key)
    }

    fun setIfAbsent(key: String, i: Int, ofSeconds: Duration) =
        redisTemplate.opsForValue().setIfAbsent(key, i.toString(), ofSeconds.toJavaDuration())
}
