package com.kakaobank.homework.blog.core.repository.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.kakaobank.homework.blog.core.domain.BlogSearch
import com.kakaobank.homework.blog.core.domain.Page
import com.kakaobank.homework.blog.core.domain.Sort
import com.kakaobank.homework.blog.core.domain.Vendor
import com.kakaobank.homework.blog.core.repository.client.kakao.KakaoApiMapper
import com.kakaobank.homework.blog.core.repository.client.kakao.KakaoBlogMapper
import com.kakaobank.homework.blog.core.repository.client.kakao.KakaoBlogMapperImpl
import com.kakaobank.homework.blog.core.repository.client.kakao.KakaoSearchResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource

@SpringBootTest(classes = [KakaoApiMapper::class, KakaoBlogMapperImpl::class, ObjectMapper::class])
internal class KakaoApiMapperTest {
  @Autowired
  private lateinit var kakaoApiMapper: KakaoApiMapper

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Test
  @DisplayName("kakao 검색 요청 맵퍼 테스트")
  fun `Test kakao request mapping`() {
    val blogSearchOrderByLatest = BlogSearch("query", Page(1, 10), Sort.LATEST)
    val blogSearchOrderByAccuracy = BlogSearch("kuckjwi~", Page(2, 10), Sort.ACCURACY)

    kakaoApiMapper.toParameter(blogSearchOrderByLatest).apply {
      val (query, page, _) = blogSearchOrderByLatest
      assertEquals(query, this["query"])
      assertEquals(page.number, this["page"])
      assertEquals(page.size, this["size"])
      assertEquals("recency", this["sort"])
    }

    kakaoApiMapper.toParameter(blogSearchOrderByAccuracy).apply {
      val (query, page, _) = blogSearchOrderByAccuracy
      assertEquals(query, this["query"])
      assertEquals(page.number, this["page"])
      assertEquals(page.size, this["size"])
      assertEquals("accuracy", this["sort"])
    }
  }

  @Test
  @DisplayName("kakao 검색 응답 맵퍼 테스트")
  fun `Test kakao response mapping`() {
    val mapper = objectMapper.copy().apply {
      propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    }.registerKotlinModule()

    val response = mapper.readValue(ClassPathResource("./mocks/kakao.json").file, KakaoSearchResponse::class.java)
    val domain = kakaoApiMapper.toDomain(response, Vendor.KAKAO)

    assertEquals(Vendor.KAKAO, domain.meta.vendor)
    assertEquals(response.meta.totalCount, domain.meta.totalCount)
    assertEquals(!response.meta.isEnd, domain.meta.hasNext)

    domain.blogs.forEachIndexed { index, blog ->
      assertEquals(response.documents[index].title, blog.title)
      assertEquals(response.documents[index].contents, blog.content)
      assertEquals(response.documents[index].url, blog.link)
      assertEquals(KakaoBlogMapper.toLocalDate(response.documents[index].datetime), blog.date)
    }
  }
}
