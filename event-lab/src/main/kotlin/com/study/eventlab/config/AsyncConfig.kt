package com.study.eventlab.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskDecorator
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.context.request.RequestContextHolder
import java.util.concurrent.Executor

@EnableAsync
@Configuration
class AsyncConfig {

    // @Value("\${thread.core-pool.size}")
    private val corePoolSize = 5
    private val maxPoolSize = 10
    private val queueCapacity = 20

    @Bean(name = ["asyncExecutor"])
    fun getAsyncExecutor(): Executor = ThreadPoolTaskExecutor().apply {
        this.corePoolSize = corePoolSize
        this.maxPoolSize = maxPoolSize
        this.queueCapacity = queueCapacity

        setWaitForTasksToCompleteOnShutdown(true)
        setAwaitTerminationSeconds(30)

        setTaskDecorator(AsyncTaskExecutor())
        setThreadNamePrefix("EVENT-EXECUTOR-")
        initialize()
    }
}

class AsyncTaskExecutor : TaskDecorator {

    override fun decorate(runnable: Runnable): Runnable {
        val requestAttribute = RequestContextHolder.currentRequestAttributes()
        return Runnable {
            RequestContextHolder.setRequestAttributes(requestAttribute)
            runnable.run()
        }
    }
}