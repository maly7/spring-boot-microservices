package io.echoseven.kryption.web

import io.echoseven.kryption.domain.UserSignup
import io.echoseven.kryption.service.UserRegistrationService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class UserRegistrationController(private val userRegistrationService: UserRegistrationService) {
    private val log = LoggerFactory.getLogger(UserRegistrationController::class.java)

    @PostMapping(path = ["/user/signup"], consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun registerUser(@Valid @RequestBody userSignup: UserSignup): UserSignup {
        log.debug("Received signup request for user with email [{}}", userSignup.email)
        return userRegistrationService.registerUser(userSignup)
    }
}
