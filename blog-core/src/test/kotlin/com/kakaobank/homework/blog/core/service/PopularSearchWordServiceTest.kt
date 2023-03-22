package com.kakaobank.homework.blog.core.service

import com.kakaobank.homework.blog.core.domain.PopularSearchWord
import com.kakaobank.homework.blog.core.repository.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
class PopularSearchWordServiceTest {
  @Mock
  internal lateinit var popularSearchWordHitRepository: PopularSearchWordHitRepository

  private lateinit var popularSearchWordService: PopularSearchWordService

  @BeforeAll
  fun init() {
    openMocks(this)
    val redisMock = mock<PopularSearchWordRedisRepository> {
      on { findTop10() }.thenReturn(emptyList())
    }
    val databaseMock = mock<PopularSearchWordJpaRepository> {
      on { findTop10() }.thenReturn(listOf(PopularSearchWord("맛집", 100, 1), PopularSearchWord("육아", 90, 2)))
    }
    popularSearchWordService =
      PopularSearchWordServiceImpl(popularSearchWordHitRepository, listOf(redisMock, databaseMock))
  }

  @Test
  @DisplayName("모종의 이유로 레디스에 인기 검색어가 없을 경우 데이터베이스에서 가져와야 한다.")
  fun `test fetch from the database`() {
    val popularSearchWords = popularSearchWordService.findTop10()
    assertEquals(2, popularSearchWords.size)
    assertEquals("맛집", popularSearchWords[0].word)
    assertEquals("육아", popularSearchWords[1].word)
  }
}
