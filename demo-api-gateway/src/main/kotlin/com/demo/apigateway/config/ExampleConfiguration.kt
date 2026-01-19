package com.demo.apigateway.config

import com.demo.apigateway.log
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class ExampleConfiguration: GlobalFilter, Ordered {


    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> {
        log().info { "custom global filter" }
        return chain.filter(exchange)
    }

    override fun getOrder(): Int {
        return -1
    }
}
