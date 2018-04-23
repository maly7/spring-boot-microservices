package io.echoseven.kryption.service

import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.security.AuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {

    fun getCurrentUser(): User {
        val currentAuth = SecurityContextHolder.getContext().authentication as AuthenticationToken
        return userRepository.findById(currentAuth.id).get()
    }
}
