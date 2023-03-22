package com.kakaobank.homework.blog.core.domain

data class BlogSearch(
  val query: String,
  val page: Page,
  val sort: Sort,
)
