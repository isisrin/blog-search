package com.kakaobank.homework.blog.api.rest.advice

import com.kakaobank.homework.blog.api.rest.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
internal class GlobalExceptionHandler {
  private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

  @ExceptionHandler(BindException::class)
  fun handleBindException(ex: BindException): ResponseEntity<ErrorResponse> {
    logger.error(ex.message, ex)
    return ResponseEntity.badRequest().body(ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.message))
  }

  @ExceptionHandler(Exception::class)
  fun handleException(ex: Exception): ResponseEntity<ErrorResponse> {
    logger.error(ex.message, ex)
    return ResponseEntity.internalServerError().body(ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.message ?: HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase))
  }
}
