package com.study.eventlab.common.redis.service

import com.study.eventlab.config.logger
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, String>
) {

    companion object {
        val log = logger()
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
        redisTemplate.opsForValue()[key] = value
    }

    fun increment(key: String): Long? {
        return redisTemplate.opsForValue().increment(key)
    }

    fun setIfAbsent(key: String, i: Int, ofSeconds: java.time.Duration) {}
}
