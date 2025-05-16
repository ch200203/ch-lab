package com.study.eventlab.command

/***
 * 내부 비즈니스용 DTO 객체
 * 웹 전달용과 내부 전달용 객체를 분리
 */
data class ClickCommand(
    val deviceId: String?,
    val gender: String,
    val age: Int,
    val ip: String,
    val userAgent: String,
) {

    var eventId: String

    init {
        eventId = createId()
    }

    /**
     * 사용할 ID 값 생셩
     */
    private fun createId(): String = (deviceId + gender + age + ip).hashCode().toString()

    /**
     * Redis 캐싱 용 키
     */
    fun cacheKey(): String {
        return "event:$eventId:click:count"
    }
}
