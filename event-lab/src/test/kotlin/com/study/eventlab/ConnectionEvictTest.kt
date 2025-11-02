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
import java.lang.Thread.sleep
import java.time.Duration

@SpringBootTest
@ActiveProfiles("local")
class ConnectionEvictTest {

    @Autowired private lateinit var lettuceConnectionFactory: LettuceConnectionFactory

    @Test
    fun `커넥션풀_evict_테스트`() {
        val connectionA = lettuceConnectionFactory.connection
        val connectionB = lettuceConnectionFactory.connection

        connectionA.openPipeline()
        connectionB.openPipeline()

        connectionA.close()
        connectionB.close()

        sleep(60000L) // 위 테스트에서는 evict 설정을 30초로 해주었으니, 여유 두어 60초정도 대기

        print("@")

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
            }
            poolConfig.minEvictableIdleDuration = Duration.ofSeconds(30L)
            poolConfig.timeBetweenEvictionRuns = Duration.ofSeconds(30L)

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
