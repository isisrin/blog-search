package com.kakaobank.homework.blog.core.entity

import com.kakaobank.homework.blog.core.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "popular_search_word")
class PopularSearchWordEntity(word: String, count: Int, rank: Int) : BaseEntity() {
  @Id
  @GeneratedValue
  var id: Int? = null

  @Column(unique = true)
  var word: String = word
    protected set

  var count: Int = count
    protected set

  var rank: Int = rank
    protected set
}
