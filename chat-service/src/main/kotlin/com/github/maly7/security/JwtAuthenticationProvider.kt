package com.github.maly7.security

import com.github.maly7.data.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(private val userRepository: UserRepository) : AuthenticationProvider {
    private val log = LoggerFactory.getLogger(JwtAuthenticationProvider::class.java)

    override fun authenticate(authentication: Authentication?): Authentication? {
        if (authentication !is AuthenticationToken) {
            log.error("Attempted to Authenticate with an object of type [{}], this should never happen", authentication?.javaClass?.simpleName)
            return authentication
        }

        val user = userRepository.findById(authentication.id).orElseThrow {
            BadCredentialsException("Unable to lookup user with provided token id ${authentication.id}")
        }

        val securityContextUser = User(user.name, authentication.token, authentication.authorities)
        log.debug("User [{}] is authenticated and ready to go", securityContextUser)

        authentication.userDetails = securityContextUser

        return authentication
    }

    override fun supports(authentication: Class<*>?) = authentication == AuthenticationToken::class.java
}
