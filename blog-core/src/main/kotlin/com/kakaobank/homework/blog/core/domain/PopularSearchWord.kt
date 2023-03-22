package com.kakaobank.homework.blog.core.domain

import com.kakaobank.homework.blog.core.entity.PopularSearchWordEntity

data class PopularSearchWord (
    val word: String,
    val count: Int,
    val rank: Int,
) {
    fun toEntity(): PopularSearchWordEntity = PopularSearchWordEntity(
        word = this.word,
        count = this.count,
        rank = this.rank,
    )
}
