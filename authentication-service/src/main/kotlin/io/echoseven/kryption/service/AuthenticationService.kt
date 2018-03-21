package io.echoseven.kryption.service

import io.echoseven.kryption.domain.UserAccount
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val userAccountService: UserAccountService,
                            private val passwordEncoder: PasswordEncoder) {

    fun authenticate(userAccount: UserAccount): String {
        return "hello"
    }
}
