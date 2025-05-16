package com.study.eventlab.config

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoroutineConfig: DisposableBean {

    companion object {
        private val log = logger()
    }

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        log.error("CoroutineExceptionHandler $throwable", throwable)
        // log.error(LogUtil.getFilterStackTrace(exception))
    }

    private val coroutineScope = CoroutineScope(job + Dispatchers.Default + exceptionHandler)

    @Bean
    fun coroutineScope() = coroutineScope

    override fun destroy() {
        job.cancel() // 스코프를 취소한다.
    }
}