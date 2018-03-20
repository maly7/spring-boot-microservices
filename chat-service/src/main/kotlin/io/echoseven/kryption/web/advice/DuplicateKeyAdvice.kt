package io.echoseven.kryption.web.advice

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class DuplicateKeyAdvice {
    private val log = LoggerFactory.getLogger(DuplicateKeyAdvice::class.java)

    @ExceptionHandler(value = [DuplicateKeyException::class])
    fun handleDuplicateKeyException(ex: DuplicateKeyException, response: HttpServletResponse) {
        log.error("Duplicate Key Exception occurred, probably due to a duplicate email during signup")
        response.sendError(BAD_REQUEST.value(), "Duplicate data provided")
    }
}
