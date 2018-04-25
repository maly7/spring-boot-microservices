package io.echoseven.kryption.service

import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.exception.BadRequestException
import io.echoseven.kryption.security.AuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {

    fun findByEmail(email: String): User =
        userRepository.findByEmail(email).orElseThrow {
            BadRequestException("No user found with email $email")
        }

    fun getCurrentUser(): User {
        val currentAuth = SecurityContextHolder.getContext().authentication as AuthenticationToken
        return userRepository.findById(currentAuth.id).get()
    }
}
