package com.kakaobank.homework.blog.core.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

interface LockRepository {
  fun lock(key: String, ttl: Long): Boolean
  fun unlock(key: String)
}

@Component
internal class RedisLockRepository(private val redisTemplate: RedisTemplate<String, Any>) : LockRepository {
  private val lockValue = "lock"

  override fun lock(key: String, ttl: Long): Boolean =
    redisTemplate.opsForValue().setIfAbsent(key, lockValue, ttl, TimeUnit.MILLISECONDS) ?: false

  override fun unlock(key: String) {
    redisTemplate.delete(key)
  }
}
