package com.kakaobank.homework.blog.core.service

import com.kakaobank.homework.blog.core.domain.BlogSearch
import com.kakaobank.homework.blog.core.domain.BlogSearchResult
import com.kakaobank.homework.blog.core.repository.BlogSearchRepository
import com.kakaobank.homework.blog.core.repository.PopularSearchWordHitRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

interface BlogSearchService {
  /**
   * 블로그 검색을 수행한다.
   *
   * @param blogSearch 블로그 검색 조건
   * @return 검색 결과
   */
  fun searchBlog(blogSearch: BlogSearch): BlogSearchResult
}

@Component
internal class FailoverBlogSearchService(
  private val blogSearchRepositories: List<BlogSearchRepository>,
) : BlogSearchService {
  private val logger = LoggerFactory.getLogger(FailoverBlogSearchService::class.java)

  @Cacheable(value = ["search"], key = "#blogSearch.hashCode()")
  override fun searchBlog(blogSearch: BlogSearch): BlogSearchResult {
    for (repository in blogSearchRepositories) {
      try {
        return repository.searchBlog(blogSearch)
      } catch(e: RuntimeException) {
        logger.error(e.message, e)
      }
    }
    return BlogSearchResult.empty()
  }
}
