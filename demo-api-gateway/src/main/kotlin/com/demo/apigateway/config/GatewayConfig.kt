package com.demo.apigateway.config

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class GatewayConfig {

    @Bean
    fun customRouteLocator(
        builder: RouteLocatorBuilder,
        redisRateLimiter: RedisRateLimiter
    ): RouteLocator {
        val uri = "http://localhost:8080"
        val httpbinorg = "http://httpbin.org"

        return builder.routes()
            .route {
                it.host("**.smpales.com").and().path("/anything/png")
                    .filters { filter ->
                        filter.prefixPath("/httpbin")
                            .addRequestHeader("X-TestHeader", "foobar")
                    }.uri(uri)
            }
            .route {
                it.host("*.exapmle.com").filters { filter ->
                    filter.circuitBreaker { cb -> cb.setName("slowcmd") }
                }.uri(httpbinorg)
            }
            .route {
                it.host("*.circuitbreakerfallback.org")
                    .filters { filter ->
                        filter.circuitBreaker { cb ->
                            cb.setName("slowcmd").setFallbackUri("forward:/circuitbreakerfallback")
                        }
                    }.uri(httpbinorg)
            }
            .route {
                it.host("*.limited.org").and().path("/anything/**")
                    .filters { filter ->
                        filter.requestRateLimiter { it.setRateLimiter(redisRateLimiter) }
                    }.uri(httpbinorg)
            }
            .build()
    }

    @Bean
    fun redisRateLimiter(): RedisRateLimiter = RedisRateLimiter(1, 2)

    @Bean
    fun springWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .httpBasic {}
            .csrf { it.disable() }
            .authorizeExchange { exchanges ->
                exchanges.pathMatchers("/anything/**").authenticated()
                    .anyExchange().permitAll()
            }
            .build()
    }
}
