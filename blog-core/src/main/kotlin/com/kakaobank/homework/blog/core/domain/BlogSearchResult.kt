package com.kakaobank.homework.blog.core.domain

import java.time.LocalDate

data class BlogSearchResult(
  val meta: Meta,
  val blogs: List<Blog>
) {
  data class Meta (
    val vendor: Vendor,
    val hasNext: Boolean,
    val totalCount: Int,
  )
  data class Blog(
    val title: String,
    val content: String,
    val link: String,
    val date: LocalDate,
  )

  companion object {
    fun empty() = BlogSearchResult(
      meta = Meta(
        hasNext = false,
        totalCount = 0,
        vendor = Vendor.UNKNOWN,
      ),
      blogs = emptyList(),
    )
  }
}
