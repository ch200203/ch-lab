package com.study.eventlab.dto

import java.time.LocalDateTime

data class EventClickLog(
    val eventId: Long,
    val deviceId: String?,           // 고유 식별자 (선택)
    val ip: String?,                 // 클라이언트 IP
    val userAgent: String?,          // 브라우저 정보
    val gender: String?,             // "M", "F", "UNKNOWN"
    val ageGroup: String?,           // "20s", "30s", ...
    val clickedAt: LocalDateTime? = LocalDateTime.now()     // 참여 시간
)
