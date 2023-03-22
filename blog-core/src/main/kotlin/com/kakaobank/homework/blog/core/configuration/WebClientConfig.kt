package com.kakaobank.homework.blog.core.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.kakaobank.homework.blog.core.configuration.routes.Router
import com.kakaobank.homework.blog.core.domain.Vendor
import com.kakaobank.homework.blog.core.repository.client.kakao.KakaoApiClient
import com.kakaobank.homework.blog.core.repository.client.naver.NaverApiClient
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Component
internal class WebClientConfig(router: Router, mapper: ObjectMapper) {

  private var factory: Map<Vendor, WebClient> = router.routes.associate {
    val vendor = Vendor.valueOf(it.id)
    vendor to WebClient.builder()
      .apply { builder ->
        builder.baseUrl(it.host)
          .clientConnector(ReactorClientHttpConnector(
            HttpClient.create()
              .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, it.timeout)
              .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(it.readTimeout, TimeUnit.MILLISECONDS))
              }
          ))
          .defaultHeaders { headers ->
            it.defaultHeaders.forEach { entry ->
              headers[entry.key] = entry.value
            }
          }
          .codecs { configurer ->
            val codec = configurer.defaultCodecs()
            when(vendor) {
              Vendor.KAKAO -> {
                val snakeCaseMapper = mapper.copy().apply {
                  propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
                }
                codec.jackson2JsonEncoder(Jackson2JsonEncoder(snakeCaseMapper))
                codec.jackson2JsonDecoder(Jackson2JsonDecoder(snakeCaseMapper))
              }
              else -> {
                codec.jackson2JsonEncoder(Jackson2JsonEncoder(mapper))
                codec.jackson2JsonDecoder(Jackson2JsonDecoder(mapper))
              }
            }
          }
      }
      .build()
  }

  @Bean
  fun kakaoApiClient(): KakaoApiClient = createClient(Vendor.KAKAO)

  @Bean
  fun naverApiClient(): NaverApiClient = createClient(Vendor.NAVER)

  private inline fun <reified T> createClient(vendor: Vendor): T = HttpServiceProxyFactory.builder()
    .apply {
      clientAdapter(
        WebClientAdapter.forClient(
          factory[vendor] ?: throw RuntimeException(T::class.simpleName + "is not available")
        )
      )
    }
    .build()
    .createClient(T::class.java)
}
