package com.kakaobank.homework.blog.core.repository.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.kakaobank.homework.blog.core.domain.BlogSearch
import com.kakaobank.homework.blog.core.domain.Page
import com.kakaobank.homework.blog.core.domain.Sort
import com.kakaobank.homework.blog.core.domain.Vendor
import com.kakaobank.homework.blog.core.repository.client.naver.NaverApiMapper
import com.kakaobank.homework.blog.core.repository.client.naver.NaverBlogMapper
import com.kakaobank.homework.blog.core.repository.client.naver.NaverBlogMapperImpl
import com.kakaobank.homework.blog.core.repository.client.naver.NaverSearchResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource

@SpringBootTest(classes = [NaverApiMapper::class, NaverBlogMapperImpl::class, ObjectMapper::class])
internal class NaverApiMapperTest {
  @Autowired
  private lateinit var naverApiMapper: NaverApiMapper

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Test
  @DisplayName("naver 검색 요청 맵퍼 테스트")
  fun `Test naver request mapping`() {
    val blogSearchOrderByLatest = BlogSearch("query", Page(1, 10), Sort.LATEST)
    val blogSearchOrderByAccuracy = BlogSearch("kuckjwi~", Page(2, 10), Sort.ACCURACY)

    naverApiMapper.toParameter(blogSearchOrderByLatest).apply {
      val (query, page, _) = blogSearchOrderByLatest
      assertEquals(query, this["query"])
      assertEquals(1, this["start"])
      assertEquals(page.size, this["display"])
      assertEquals("date", this["sort"])
    }

    naverApiMapper.toParameter(blogSearchOrderByAccuracy).apply {
      val (query, page, _) = blogSearchOrderByAccuracy
      assertEquals(query, this["query"])
      assertEquals(11, this["start"])
      assertEquals(page.size, this["display"])
      assertEquals("sim", this["sort"])
    }
  }

  @Test
  @DisplayName("naver 검색 응답 맵퍼 테스트")
  fun `Test naver response mapping`() {
    val mapper = objectMapper.copy().registerKotlinModule()
    val response = mapper.readValue(ClassPathResource("./mocks/naver.json").file, NaverSearchResponse::class.java)
    val domain = naverApiMapper.toDomain(response, Vendor.NAVER)

    assertEquals(Vendor.NAVER, domain.meta.vendor)
    assertEquals(response.total, domain.meta.totalCount)
    assertEquals(true, domain.meta.hasNext)

    domain.blogs.forEachIndexed { index, blog ->
      assertEquals(response.items[index].title, blog.title)
      assertEquals(response.items[index].description, blog.content)
      assertEquals(response.items[index].link, blog.link)
      assertEquals(NaverBlogMapper.toLocalDate(response.items[index].postdate), blog.date)
    }
  }
}
