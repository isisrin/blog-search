package com.kakaobank.homework.blog.core.service

import com.kakaobank.homework.blog.core.repository.PopularSearchWordJpaRepository
import com.kakaobank.homework.blog.core.repository.PopularSearchWordRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

interface PopularSearchWordSyncService {
  /**
   * 인기 검색어를 데이터베이스에 동기화 한다.
   */
  fun sync()
}

@Component
internal class PopularSearchWordSyncServiceImpl(
  private val popularSearchWordJpaRepository: PopularSearchWordJpaRepository,
  private val popularSearchWordRepository: PopularSearchWordRepository,
) : PopularSearchWordSyncService {
  @Transactional
  override fun sync() {
    popularSearchWordJpaRepository.deleteAllInBatch()
    popularSearchWordJpaRepository.saveAll(popularSearchWordRepository.findTop10().map { it.toEntity() })
  }
}
