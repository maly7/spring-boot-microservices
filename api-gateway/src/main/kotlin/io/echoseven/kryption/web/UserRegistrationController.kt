package io.echoseven.kryption.web

import io.echoseven.kryption.domain.UserRegistration
import io.echoseven.kryption.domain.UserResponse
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

    @PostMapping(path = ["/user/registration"], consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun registerUser(@Valid @RequestBody userRegistration: UserRegistration): UserResponse {
        log.debug("Received registration request for user with email [{}}", userRegistration.email)
        return userRegistrationService.registerUser(userRegistration)
    }
}
