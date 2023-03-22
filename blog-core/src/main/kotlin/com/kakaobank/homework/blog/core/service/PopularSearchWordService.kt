package com.kakaobank.homework.blog.core.service

import com.kakaobank.homework.blog.core.domain.PopularSearchWord
import com.kakaobank.homework.blog.core.repository.PopularSearchWordHitRepository
import com.kakaobank.homework.blog.core.repository.PopularSearchWordRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

interface PopularSearchWordService {
  /**
   * 검색 시 인기검색을 산정을 위하여 단어를 hit 한다.
   *
   * 현재 비동기로 별도의 스레드에서 처리하도록 구현되어 있다.
   */
  fun hit(word: String)

  /**
   * 인기검색어 상위 10건을 조회한다.
   */
  fun findTop10(): List<PopularSearchWord>
}

@Component
internal class PopularSearchWordServiceImpl(
  private val popularSearchWordHitRepository: PopularSearchWordHitRepository,
  private val popularSearchWordRepositories: List<PopularSearchWordRepository>,
) : PopularSearchWordService {
  @Async("hitExecutor")
  override fun hit(word: String) {
    popularSearchWordHitRepository.hit(word)
  }

  override fun findTop10(): List<PopularSearchWord> {
    for (repository in popularSearchWordRepositories) {
      val top10Words = repository.findTop10()
      if (top10Words.isNotEmpty()) {
        return top10Words
      }
    }
    return emptyList()
  }
}
