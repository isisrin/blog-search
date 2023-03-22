package com.kakaobank.homework.blog.core.configuration

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = ["com.kakaobank.homework.blog.core.entity"])
@EnableJpaRepositories(basePackages = ["com.kakaobank.homework.blog.core.repository"])
class JpaConfig
