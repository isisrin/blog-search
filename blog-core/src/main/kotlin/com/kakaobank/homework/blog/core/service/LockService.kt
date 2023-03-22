package com.kakaobank.homework.blog.core.service

import com.kakaobank.homework.blog.core.repository.LockRepository
import org.springframework.stereotype.Component

interface LockService {
  fun lock(key: String, ttl: Long): Boolean
  fun unlock(key: String)
}

@Component
internal class LockServiceImpl(private val lockRepository: LockRepository) : LockService {
  override fun lock(key: String, ttl: Long): Boolean = lockRepository.lock(key, ttl)
  override fun unlock(key: String) = lockRepository.unlock(key)
}
