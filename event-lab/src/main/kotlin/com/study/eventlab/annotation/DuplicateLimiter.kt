package com.study.eventlab.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DuplicateLimiter(
    val ttsSeconds: Long = 3600, // 기본값은 1시간
)
