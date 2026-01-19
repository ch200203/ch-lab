package com.demo.apigateway

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.gateway.route.RouteLocator
import reactor.test.StepVerifier

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApiGatewayApplicationTests @Autowired constructor(
    private val routeLocator: RouteLocator
) {

    @Test
    @DisplayName("애플리케이션 컨텍스트가 정상적으로 기동된다")
    fun contextLoads() {
        assertThat(routeLocator).isNotNull
    }

    @Test
    @DisplayName("등록된 라우트가 모두 생성된다")
    fun registerRoutes() {
        val routes = routeLocator.routes

        StepVerifier.create(routes.collectList())
            .assertNext { list ->
                assertThat(list).hasSize(4)
                assertThat(list.map { it.uri.toString() })
                    .contains("http://localhost:8080", "http://httpbin.org")
            }
            .verifyComplete()
    }

    @Test
    @DisplayName("회로 차단기 폴백이 기대한 문자열을 반환한다")
    fun circuitBreakerFallbackReturnsMessage() {
        assertThat(circuitbreakerfallback()).isEqualTo("This is a fallback")
    }
}
