package com.kakaobank.homework.blog.core.service

import com.kakaobank.homework.blog.core.domain.*
import com.kakaobank.homework.blog.core.repository.KakaoSearchRepository
import com.kakaobank.homework.blog.core.repository.NaverSearchRepository
import com.kakaobank.homework.blog.core.repository.PopularSearchWordHitRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
internal class BlogSearchServiceFailoverTest {
  @Mock
  internal lateinit var popularSearchWordHitRepository: PopularSearchWordHitRepository

  private lateinit var failoverBlogSearchService: BlogSearchService

  @BeforeAll
  fun init() {
    openMocks(this)
    val kakaoMock = mock<KakaoSearchRepository> {
      on { searchBlog(any()) }.thenThrow(RuntimeException("Unable to connect to kakao api"))
    }
    val naverMock = mock<NaverSearchRepository> {
      on { searchBlog(any()) }.thenReturn(BlogSearchResult(
        BlogSearchResult.Meta(Vendor.NAVER, true, 100),
        emptyList(),
      ))
    }
    failoverBlogSearchService = FailoverBlogSearchService(listOf(kakaoMock, naverMock))
  }

  @Test
  @DisplayName("카카오 블로그 검색 서비스 장애 시 네이버 블로그 검색 서비스로 대체되어야 한다")
  fun `Test failover behavior`() {
    val blogSearch = BlogSearch(
      "blog", Page(1, 10), Sort.ACCURACY
    )
    assertDoesNotThrow {
      val searchResult = failoverBlogSearchService.searchBlog(blogSearch)
      assertEquals(Vendor.NAVER, searchResult.meta.vendor)
    }
  }
}
