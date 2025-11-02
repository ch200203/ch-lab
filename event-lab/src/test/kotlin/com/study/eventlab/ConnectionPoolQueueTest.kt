package com.study.eventlab

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("local")
class ConnectionPoolQueueTest {

    @Autowired
    private lateinit var lettuceConnectionFactory: LettuceConnectionFactory

    @Test
    fun `커넥션풀이 fifo인 경우를 테스트한다`() {
        val aRedisConnection = lettuceConnectionFactory.connection
        val bRedisConnection = lettuceConnectionFactory.connection

        aRedisConnection.openPipeline()
        bRedisConnection.openPipeline()

        val aConnection = aRedisConnection.nativeConnection
        val bConnection = bRedisConnection.nativeConnection

        // a, b 커넥션을 종료시킴
        aRedisConnection.close()
        bRedisConnection.close()

        val connectionC = lettuceConnectionFactory.connection
        val connectionD = lettuceConnectionFactory.connection

        connectionC.openPipeline()
        connectionD.openPipeline()

        // fifo 이므로 C, D 커넥션 할당 시에 => A, B 커넥션이 다시 할당된다
        assert(aConnection == connectionC.nativeConnection)
        assert(bConnection == connectionD.nativeConnection)
    }

    @TestConfiguration
    class RedisPoolConfig {
        @Bean
        fun connectionPool(redisProperties: RedisProperties): LettuceConnectionFactory {
            // Configure commons-pool2 settings (FIFO by setting lifo = false)
            val poolProps = redisProperties.lettuce.pool
            val poolConfig = GenericObjectPoolConfig<io.lettuce.core.api.StatefulRedisConnection<String, String>>().apply {
                poolProps.maxActive?.let { maxTotal = it }
                poolProps.maxIdle?.let { maxIdle = it }
                poolProps.minIdle?.let { minIdle = it }
                poolProps.maxWait?.let { setMaxWait(it) }
                lifo = false // use FIFO order
            }

            val clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .build()

            val standalone = RedisStandaloneConfiguration().apply {
                hostName = redisProperties.host
                port = redisProperties.port
                database = redisProperties.database
                if (!redisProperties.password.isNullOrEmpty()) {
                    password = RedisPassword.of(redisProperties.password)
                }
            }

            return LettuceConnectionFactory(standalone, clientConfig)
        }
    }
}
