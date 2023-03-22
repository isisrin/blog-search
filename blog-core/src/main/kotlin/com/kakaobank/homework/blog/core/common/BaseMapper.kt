package com.kakaobank.homework.blog.core.common

interface BaseMapper<L, R> {
  fun of(left: L): R

  fun listOf(lefts: List<L>): List<R> = lefts.map { of(it) }
}
