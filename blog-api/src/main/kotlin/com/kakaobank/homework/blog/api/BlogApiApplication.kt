package com.kakaobank.homework.blog.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.kakaobank.homework.blog"])
@ConfigurationPropertiesScan(basePackages = ["com.kakaobank.homework.blog"])
class BlogApplication

fun main(args: Array<String>) {
  runApplication<BlogApplication>(*args)
}
