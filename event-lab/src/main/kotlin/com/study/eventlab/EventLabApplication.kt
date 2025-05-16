package com.study.eventlab

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventLabApplication

fun main(args: Array<String>) {
	runApplication<EventLabApplication>(*args)
}
