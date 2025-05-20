package com.study.eventlab.annotation

import com.study.eventlab.common.redis.service.RedisService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.Duration.*
import kotlin.time.Duration.Companion.seconds

// @Aspect
// @Component
class DuplicateLimiterAspect(
    private val redisService: RedisService,
) {

    // @Around("@annotation(DuplicateLimiter)")
    fun checkLimits(joinPoint: ProceedingJoinPoint, duplicateLimiter: DuplicateLimiter): Any? {
        val request = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val ip = request.request.remoteAddr;
        val userAgent = request.request.getHeader("User-Agent") ?: "unknown"

        val signature = joinPoint.signature as MethodSignature
        val methodName = signature.method.name
        val key = "ratelimit:${methodName}:${createCacheKey(ip, userAgent)}"

        val currentCount = redisService["event:total:count:"]?.toLong()

        val allowed = redisService.setIfAbsent(key, 1, duplicateLimiter.ttsSeconds.seconds)

        // TODO isLimit 함수 개발 필요

        return joinPoint.proceed()
    }


    private fun createCacheKey(ip: String, userAgent: String) = buildString {
        append(ip).append(':').append(userAgent)
    }.hashCode()

}