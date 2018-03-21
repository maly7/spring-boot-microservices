package io.echoseven.kryption.service

import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.exception.UnauthorizedException
import io.echoseven.kryption.exception.UserNotFoundException
import io.echoseven.kryption.tokens.TokenIssuer
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val userAccountService: UserAccountService,
                            private val tokenIssuer: TokenIssuer,
                            private val passwordEncoder: PasswordEncoder) {

    fun authenticate(authRequest: UserAccount): String {
        val userAccountOptional = userAccountService.get(authRequest.email!!)

        if (!userAccountOptional.isPresent) {
            throw UserNotFoundException("No user found with email: " + authRequest.email)
        }

        val existingUser = userAccountOptional.get()

        if (!passwordEncoder.matches(authRequest.password, existingUser.password)) {
            throw UnauthorizedException("Password provided does not match")
        }

        return tokenIssuer.issueToken(existingUser)
    }
}
