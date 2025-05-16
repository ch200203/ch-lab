package com.study.eventlab.controller

import com.study.eventlab.config.logger
import com.study.eventlab.dto.ClickRequestDto
import com.study.eventlab.dto.ClickResponseDto
import com.study.eventlab.service.EventService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController(
    private val eventService: EventService
) {

    companion object {
        val log = logger()
    }

    @PostMapping("/event/{eventId}/clicks")
    fun clickEvent(
        @PathVariable eventId: Long,
        @RequestBody request: ClickRequestDto,
        httpRequest: HttpServletRequest
    ): ResponseEntity<ClickResponseDto> {
        val clickCommand = request.toCommand(httpRequest.remoteAddr, httpRequest.getHeader("User-Agent") ?: "")
        val response = eventService.click(clickCommand)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/event/{eventId}")
    fun getEventTotalCount(@PathVariable eventId: Long): Long {
        return eventService.getTotalCount()
    }
}