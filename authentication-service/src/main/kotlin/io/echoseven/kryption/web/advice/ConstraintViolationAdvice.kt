package io.echoseven.kryption.web.advice

import org.hibernate.exception.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class ConstraintViolationAdvice {
    private val log = LoggerFactory.getLogger(ConstraintViolationAdvice::class.java)

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleConstraintViolation(ex: ConstraintViolationException, response: HttpServletResponse) {
        log.error("SQL Constraint violation [{}]", ex.constraintName)
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.localizedMessage)
    }
}
