package com.kakaobank.homework.blog.core.repository.client.kakao

import java.time.LocalDateTime

internal data class KakaoSearchResponse(
  val meta: Meta,
  val documents: List<Document>,
) {
  data class Meta(
    val pageableCount: Int,
    val totalCount: Int,
    val isEnd: Boolean,
  )

  data class Document(
    val blogname: String,
    val contents: String,
    val datetime: String,
    val thumbnail: String,
    val title: String,
    val url: String,
  )
}
