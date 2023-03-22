package com.kakaobank.homework.blog.core.repository.client.naver

internal data class NaverSearchResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<Item>,
) {
    data class Item(
        val title: String,
        val link: String,
        val description: String,
        val bloggername: String,
        val bloggerlink: String,
        val postdate: String,
    )
}
