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

    // DB 에서 구분하기 위한 UniqueKey 값임.
    var eventId: String

    init {
        eventId = createId()
    }

    /**
     * 사용할 ID 값 생셩 => 이걸 기반으로 중복을 걸러야함
     */
    private fun createId(): String = (deviceId + gender + age + ip).hashCode().toString()

    // 아오 이거 이름 꼬였네 뭐라고 하냐
    fun cacheKey(): String {
        return "event:click:count:"
    }
}
