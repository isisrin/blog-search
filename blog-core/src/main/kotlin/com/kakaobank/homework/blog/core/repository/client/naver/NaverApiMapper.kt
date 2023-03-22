package com.kakaobank.homework.blog.core.repository.client.naver

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
internal interface NaverBlogMapper : BaseMapper<NaverSearchResponse.Item, BlogSearchResult.Blog> {
  @Mappings(
    Mapping(source = "title", target = "title"),
    Mapping(source = "description", target = "content"),
    Mapping(source = "link", target = "link"),
    Mapping(source = "postdate", target = "date", qualifiedByName = ["toLocalDate"]),
  )
  override fun of(left: NaverSearchResponse.Item): BlogSearchResult.Blog

  companion object {
    @JvmStatic
    @Named("toLocalDate")
    fun toLocalDate(postdate: String): LocalDate = LocalDate.parse(postdate, DateTimeFormatter.ofPattern("yyyyMMdd"))
  }
}

@Component
internal class NaverApiMapper(private val naverBlogMapper: NaverBlogMapper) {
  fun toParameter(blogSearch: BlogSearch): Map<String, Any> {
    val (query, page, sort) = blogSearch
    return mapOf(
      "query" to query,
      "display" to page.size,
      "start" to (page.number * page.size + 1) - page.size,
      "sort" to when(sort) {
        Sort.ACCURACY -> "sim"
        Sort.LATEST -> "date"
      },
    )
  }

  fun toDomain(response: NaverSearchResponse, vendor: Vendor): BlogSearchResult {
    val (_, total, start, display, items) = response
    return BlogSearchResult(
      meta = BlogSearchResult.Meta(
        vendor = vendor,
        totalCount = total,
        hasNext = total > start + display - 1,
      ),
      blogs = items.map(naverBlogMapper::of),
    )
  }
}
