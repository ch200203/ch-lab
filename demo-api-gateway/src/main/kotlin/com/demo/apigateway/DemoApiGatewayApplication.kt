package com.demo.apigateway

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RequestMapping


@SpringBootApplication
class DemoApiGatewayApplication

fun main(args: Array<String>) {
    runApplication<DemoApiGatewayApplication>(*args)
}

@RequestMapping("/circuitbreakerfallback")
fun circuitbreakerfallback(): String {
    return "This is a fallback"
}

inline fun <reified T : Any> T.log(): KLogger = KotlinLogging.logger {}
