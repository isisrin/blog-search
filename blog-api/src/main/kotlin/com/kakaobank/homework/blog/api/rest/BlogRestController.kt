package com.kakaobank.homework.blog.api.rest

import com.kakaobank.homework.blog.api.rest.request.BlogSearchRequest
import com.kakaobank.homework.blog.core.domain.BlogSearchResult
import com.kakaobank.homework.blog.core.domain.PopularSearchWord
import com.kakaobank.homework.blog.core.service.BlogSearchService
import com.kakaobank.homework.blog.core.service.PopularSearchWordService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/blogs")
internal class BlogRestController(
  private val blogSearchService: BlogSearchService,
  private val popularSearchWordService: PopularSearchWordService,
) {
  @GetMapping("/search")
  fun searchBlog(@Valid request: BlogSearchRequest): ResponseEntity<BlogSearchResult> {
    val blogSearch = request.toDomain()
    return ResponseEntity.ok(blogSearchService.searchBlog(blogSearch).apply {
      popularSearchWordService.hit(blogSearch.query)
    })
  }

  @GetMapping("/popular-search-words")
  fun getPopularSearchWords(): ResponseEntity<List<PopularSearchWord>> {
    return ResponseEntity.ok(popularSearchWordService.findTop10())
  }
}
