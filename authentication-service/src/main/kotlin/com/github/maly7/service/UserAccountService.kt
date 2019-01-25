package com.github.maly7.service

import com.github.maly7.data.UserAccountRepository
import com.github.maly7.domain.UserAccount
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserAccountService(
    private val userAccountRepository: UserAccountRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun create(user: UserAccount): UserAccount {
        user.password = passwordEncoder.encode(user.password)
        return userAccountRepository.save(user)
    }

    fun getByEmail(email: String) = userAccountRepository.findByEmail(email)

    fun getById(id: String) = userAccountRepository.findById(id)

    // TODO: remove with #19
    fun deleteAllUsers() = userAccountRepository.deleteAll()
}
