package com.demo.apigateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class RouterConfig {

    @Bean
    fun mainRouter() = router {
        accept(TEXT_PLAIN).nest {
            GET("/gateway/health") {
                ServerResponse.ok().contentType(TEXT_PLAIN).bodyValue("gateway-ok")
            }
        }
    }
}
