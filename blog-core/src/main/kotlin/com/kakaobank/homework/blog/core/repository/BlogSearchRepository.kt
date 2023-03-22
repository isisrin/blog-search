package com.kakaobank.homework.blog.core.repository

import com.kakaobank.homework.blog.core.domain.BlogSearch
import com.kakaobank.homework.blog.core.domain.BlogSearchResult
import com.kakaobank.homework.blog.core.domain.Vendor
import com.kakaobank.homework.blog.core.repository.client.kakao.KakaoApiClient
import com.kakaobank.homework.blog.core.repository.client.kakao.KakaoApiMapper
import com.kakaobank.homework.blog.core.repository.client.naver.NaverApiClient
import com.kakaobank.homework.blog.core.repository.client.naver.NaverApiMapper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

interface BlogSearchRepository {
  fun searchBlog(blogSearch: BlogSearch): BlogSearchResult

  fun vendor(): Vendor
}

@Component
@Order(1)
internal class KakaoSearchRepository(private val kakaoApiClient: KakaoApiClient, private val kakaoApiMapper: KakaoApiMapper) :
  BlogSearchRepository {
  override fun searchBlog(blogSearch: BlogSearch): BlogSearchResult =
    kakaoApiMapper.toDomain(kakaoApiClient.searchBlog(kakaoApiMapper.toParameter(blogSearch)), vendor())

  override fun vendor(): Vendor = Vendor.KAKAO
}

@Component
@Order(2)
internal class NaverSearchRepository(private val naverApiClient: NaverApiClient, private val naverApiMapper: NaverApiMapper) :
  BlogSearchRepository {
  override fun searchBlog(blogSearch: BlogSearch): BlogSearchResult =
    naverApiMapper.toDomain(naverApiClient.searchBlog(naverApiMapper.toParameter(blogSearch)), vendor())

  override fun vendor(): Vendor = Vendor.NAVER
}
