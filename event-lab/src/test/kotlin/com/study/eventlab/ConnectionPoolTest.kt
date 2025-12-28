package com.study.eventlab

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@SpringBootTest
@ActiveProfiles("local")
class ConnectionPoolTest {

    @Autowired
    lateinit var lettuceConnectionFactory: LettuceConnectionFactory

    @Test
    fun pipeline을_사용하지_않는_경우_기본적으로_동일한_커넥션을_사용한다() {

        val connectionA = lettuceConnectionFactory.connection
        val connectionB = lettuceConnectionFactory.connection

        assertEquals(connectionA.nativeConnection, connectionB.nativeConnection)

    }

    @Test
    fun `pipeline을 사용하는경우 전용 커넥션을 사용한다`() {
        val connectionA = lettuceConnectionFactory.connection
        val connectionB = lettuceConnectionFactory.connection

        connectionA.openPipeline()
        connectionB.openPipeline()

        assertNotEquals(connectionA.nativeConnection, connectionB.nativeConnection)
    }
}
