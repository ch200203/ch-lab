package com.study.eventlab.infrastructure

interface EventRepository {

    // 저장을 위한 인터페이스
    fun save(): Int

    // JPA or Feign client 호출하는 형태로 개발할 수 있게 해야함
}