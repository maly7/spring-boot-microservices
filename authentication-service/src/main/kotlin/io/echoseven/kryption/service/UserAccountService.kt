package io.echoseven.kryption.service

import io.echoseven.kryption.data.UserAccountRepository
import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.tokens.TokenIssuer
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserAccountService(private val userAccountRepository: UserAccountRepository,
                         private val passwordEncoder: PasswordEncoder,
                         private val tokenIssuer: TokenIssuer) {

    fun create(user: UserAccount): UserAccount {
        user.password = passwordEncoder.encode(user.password)
        return userAccountRepository.save(user)
    }

    fun get(email: String) = userAccountRepository.findByEmail(email)

    fun getFromToken(token: String): Optional<UserAccount> {
        val id = tokenIssuer.getIdFromToken(token)
        return userAccountRepository.findById(id)
    }

    // TODO: remove with #19
    fun deleteAllUsers() = userAccountRepository.deleteAll()
}
