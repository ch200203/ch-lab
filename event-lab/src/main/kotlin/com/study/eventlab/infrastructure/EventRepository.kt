package com.study.eventlab.infrastructure

import com.study.eventlab.model.EventModel
import org.springframework.data.repository.CrudRepository

interface EventRepository : CrudRepository<EventModel, Long> {
    fun findAllById(id: Long): List<EventModel>
    // JPA or Feign client 호출하는 형태로 개발할 수 있게 해야함
}