package com.kakaobank.homework.blog.core.configuration

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class LocalCacheConfig {
  @Bean
  fun caffeineCacheConfig(): Caffeine<Any, Any> = Caffeine.newBuilder()
    .maximumSize(1000)
    .expireAfterWrite(5, TimeUnit.MINUTES)

  @Bean
  fun blogCacheManager(cache: Caffeine<Any, Any>): CacheManager =
    CaffeineCacheManager().apply {
      setCaffeine(cache)
    }
}
