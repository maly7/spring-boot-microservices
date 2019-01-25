package com.github.maly7.web.advice

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class DuplicateKeyAdvice : ResponseEntityExceptionHandler() {
    private val log = LoggerFactory.getLogger(DuplicateKeyAdvice::class.java)

    @ExceptionHandler(value = [DuplicateKeyException::class])
    fun handleDuplicateKeyException(ex: DuplicateKeyException, request: WebRequest): ResponseEntity<Any> {
        log.error("Duplicate Key Exception occurred, probably due to a duplicate email during registration")
        return handleExceptionInternal(ex, "Duplicate Entry", HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }
}
