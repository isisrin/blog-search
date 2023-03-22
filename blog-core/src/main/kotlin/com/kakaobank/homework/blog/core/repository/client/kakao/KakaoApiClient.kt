package com.kakaobank.homework.blog.core.repository.client.kakao

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

internal interface KakaoApiClient {
  @GetExchange("/v2/search/blog")
  fun searchBlog(@RequestParam request: Map<String, Any>): KakaoSearchResponse
}
