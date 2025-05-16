package com.study.eventlab.dto

import com.study.eventlab.command.ClickCommand

data class ClickRequestDto(
    val deviceId: String,          // optional, 프론트에서 로컬 생성 가능
    val gender: String,            // "M", "F", or
    val age: Int           // "10s", "20s", ...,
) {
    fun toCommand(ip: String, userAgent: String): ClickCommand {
        return ClickCommand(
            deviceId = deviceId,
            gender = gender,
            age = age,
            ip = ip,
            userAgent = userAgent,
        )
    }
}

data class ClickResponseDto(
    val message: String,
    val clickCount: Long,
    val totalCount: Long,
)
