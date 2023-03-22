package com.kakaobank.homework.blog.core.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
class AsyncConfig {
  @Bean
  fun hitExecutor(): Executor {
    val processor = Runtime.getRuntime().availableProcessors()
    val threadPoolExecutor = ThreadPoolTaskExecutor()
    threadPoolExecutor.corePoolSize = processor
    threadPoolExecutor.maxPoolSize = processor * 2
    threadPoolExecutor.queueCapacity = 10000
    threadPoolExecutor.initialize()
    return threadPoolExecutor
  }
}
