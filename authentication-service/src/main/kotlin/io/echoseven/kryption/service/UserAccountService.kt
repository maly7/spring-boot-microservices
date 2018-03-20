package io.echoseven.kryption.service

import io.echoseven.kryption.data.UserAccountRepository
import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.web.resource.UserAccountResource
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserAccountService(private val userAccountRepository: UserAccountRepository,
                         private val passwordEncoder: PasswordEncoder) {

    fun createUser(user: UserAccount): UserAccountResource {
        user.password = passwordEncoder.encode(user.password)
        return UserAccountResource(userAccountRepository.save(user))
    }

    // TODO: remove with #19
    fun deleteAllUsers() = userAccountRepository.deleteAll()
}
