package com.kakaobank.homework.blog.core.repository

import com.kakaobank.homework.blog.core.domain.PopularSearchWord
import com.kakaobank.homework.blog.core.entity.PopularSearchWordEntity
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

interface PopularSearchWordHitRepository {
  fun hit(word: String)
}

interface PopularSearchWordRepository {
  fun findTop10(): List<PopularSearchWord>
}

// jpa
internal interface PopularSearchWordJpaRepository : PopularSearchWordRepository,
  JpaRepository<PopularSearchWordEntity, Int> {
  @Query("SELECT new com.kakaobank.homework.blog.core.domain.PopularSearchWord(psw.word, psw.count, psw.rank) FROM PopularSearchWordEntity psw ORDER BY psw.rank ASC")
  override fun findTop10(): List<PopularSearchWord>
}

// redis
@Component
@Primary
internal class PopularSearchWordRedisRepository(private val redisTemplate: RedisTemplate<String, Any>) :
  PopularSearchWordHitRepository, PopularSearchWordRepository {
  private val popularSearchWordKey = "popular-search-word-set"

  override fun hit(word: String) {
    redisTemplate.opsForZSet().incrementScore(popularSearchWordKey, word, 1.0)
  }

  override fun findTop10(): List<PopularSearchWord> {
    return redisTemplate.opsForZSet()
      .reverseRangeWithScores(popularSearchWordKey, 0, 9)
      ?.mapIndexed { index, tuple ->
        PopularSearchWord(tuple.value!!.toString(), tuple.score!!.toInt(), index + 1)
      } ?: emptyList()
  }
}
