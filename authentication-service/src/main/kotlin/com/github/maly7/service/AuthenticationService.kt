package com.github.maly7.service

import com.github.maly7.domain.UserAccount
import com.github.maly7.exception.UnauthorizedException
import com.github.maly7.exception.UserNotFoundException
import com.github.maly7.tokens.TokenIssuer
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class AuthenticationService(
    private val userAccountService: UserAccountService,
    private val tokenIssuer: TokenIssuer,
    private val passwordEncoder: PasswordEncoder
) {
    private val log = LoggerFactory.getLogger(AuthenticationService::class.java)

    fun authenticate(authRequest: UserAccount): String {
        val requestEmail = authRequest.email!!
        log.debug("Received login request for user email: [{}]", requestEmail)

        val userAccountOptional = userAccountService.getByEmail(requestEmail)

        if (!userAccountOptional.isPresent) {
            log.warn("Failed login attempt for email [{}] because the user was not found", requestEmail)
            throw UserNotFoundException("No user found with email: $requestEmail")
        }

        val existingUser = userAccountOptional.get()

        if (!passwordEncoder.matches(authRequest.password, existingUser.password)) {
            log.debug("Failed login attempt for email [{}] due to bad password", requestEmail)
            throw UnauthorizedException("Password provided does not match")
        }

        return tokenIssuer.issueToken(existingUser)
    }

    fun authenticate(token: String): Optional<UserAccount> {
        val id = tokenIssuer.getIdFromToken(token)
        return userAccountService.getById(id)
    }
}
