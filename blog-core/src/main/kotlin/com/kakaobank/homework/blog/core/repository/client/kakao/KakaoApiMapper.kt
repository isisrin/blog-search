package com.kakaobank.homework.blog.core.repository.client.kakao

import com.kakaobank.homework.blog.core.common.BaseMapper
import com.kakaobank.homework.blog.core.domain.BlogSearch
import com.kakaobank.homework.blog.core.domain.BlogSearchResult
import com.kakaobank.homework.blog.core.domain.Sort
import com.kakaobank.homework.blog.core.domain.Vendor
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Mapper(componentModel = "spring")
internal interface KakaoBlogMapper : BaseMapper<KakaoSearchResponse.Document, BlogSearchResult.Blog> {
  @Mappings(
    Mapping(source = "title", target = "title"),
    Mapping(source = "contents", target = "content"),
    Mapping(source = "url", target = "link"),
    Mapping(source = "datetime", target = "date", qualifiedByName = ["toLocalDate"]),
  )
  override fun of(left: KakaoSearchResponse.Document): BlogSearchResult.Blog

  companion object {
    @JvmStatic
    @Named("toLocalDate")
    fun toLocalDate(datetime: String): LocalDate = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")).toLocalDate()
  }
}

@Component
internal class KakaoApiMapper(private val kakaoBlogMapper: KakaoBlogMapper) {

  fun toParameter(blogSearch: BlogSearch): Map<String, Any> {
    val (query, page, sort) = blogSearch
    return mapOf(
      "query" to query,
      "page" to page.number,
      "size" to page.size,
      "sort" to when (sort) {
        Sort.ACCURACY -> "accuracy"
        Sort.LATEST -> "recency"
      },
    )
  }

  fun toDomain(response: KakaoSearchResponse, vendor: Vendor): BlogSearchResult {
    val (meta, documents) = response
    return BlogSearchResult(
      meta = BlogSearchResult.Meta(
        hasNext = !meta.isEnd,
        totalCount = meta.totalCount,
        vendor = vendor,
      ),
      blogs = kakaoBlogMapper.listOf(documents),
    )
  }
}
