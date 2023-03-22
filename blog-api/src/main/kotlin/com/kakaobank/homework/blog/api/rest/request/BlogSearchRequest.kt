package com.kakaobank.homework.blog.api.rest.request

import com.kakaobank.homework.blog.core.domain.BlogSearch
import com.kakaobank.homework.blog.core.domain.Page
import com.kakaobank.homework.blog.core.domain.Sort
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

internal data class BlogSearchRequest(
  @field:NotBlank(message = "쿼리는 필수값 입니다.")
  val query: String,

  @field:Min(1)
  @field:Max(50)
  val page: Int,

  @field:Min(1)
  @field:Max(50)
  val size: Int,

  val sort: Sort,
) {
  fun toDomain(): BlogSearch = BlogSearch(
    query = this@BlogSearchRequest.query,
    page = Page(this@BlogSearchRequest.page, this@BlogSearchRequest.size),
    sort = this@BlogSearchRequest.sort,
  )
}
