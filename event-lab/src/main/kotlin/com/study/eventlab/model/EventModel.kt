package com.study.eventlab.model

import com.study.eventlab.command.ClickCommand
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "events")
class EventModel(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,
    val eventId: String,
    val age: Int,
    val gender: String,
    val userAgent: String,
    val ip: String,
    val registerDateTime: LocalDateTime,
) {

    companion object {
        fun from(clickCommand: ClickCommand) = EventModel(
            eventId = clickCommand.eventId,
            age = clickCommand.age,
            gender = clickCommand.gender,
            userAgent = clickCommand.userAgent,
            ip = clickCommand.ip,
            registerDateTime = LocalDateTime.now(),
        )
    }

}
