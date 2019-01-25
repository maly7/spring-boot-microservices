package com.github.maly7.web

import com.github.maly7.domain.UserAuth
import com.github.maly7.domain.UserResponse
import com.github.maly7.service.UserRegistrationService
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
    fun registerUser(@Valid @RequestBody userAuth: UserAuth): UserResponse {
        log.debug("Received registration request for user with email [{}]", userAuth.email)
        return userRegistrationService.registerUser(userAuth)
    }
}
