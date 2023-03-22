package com.kakaobank.homework.blog.core.repository.client.naver

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

internal interface NaverApiClient {
  @GetExchange("/v1/search/blog.json")
  fun searchBlog(@RequestParam request: Map<String, Any>): NaverSearchResponse
}
