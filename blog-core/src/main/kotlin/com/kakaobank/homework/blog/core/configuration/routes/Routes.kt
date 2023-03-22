package com.kakaobank.homework.blog.core.configuration.routes

import org.springframework.boot.context.properties.ConfigurationProperties

internal data class Route(val id: String, val host: String, val timeout: Int, val readTimeout: Long, val defaultHeaders: Map<String, String>)

@ConfigurationProperties(prefix = "router")
internal data class Router(val routes: List<Route>)
