package io.echoseven.kryption.web.advice

import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

//@ControllerAdvice
class FeignExceptionAdvice : ResponseEntityExceptionHandler() {
    private val log = LoggerFactory.getLogger(FeignExceptionAdvice::class.java)

    @ExceptionHandler(value = [FeignException::class])
    fun handleFeignException(ex: FeignException, request: WebRequest): ResponseEntity<Any> {
        log.error("Feign Exception occurred on Request [{}], bubbling up status code [{}]", request.contextPath, ex.status())
        return handleExceptionInternal(ex, ex.message, HttpHeaders(), HttpStatus.valueOf(ex.status()), request)
    }
}
