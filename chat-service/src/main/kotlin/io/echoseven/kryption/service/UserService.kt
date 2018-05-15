package io.echoseven.kryption.service

import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.exception.BadRequestException
import io.echoseven.kryption.exception.NotFoundException
import io.echoseven.kryption.security.AuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {

    fun get(id: String): User =
        userRepository.findById(id).orElseThrow { NotFoundException("No User Found with id $id") }

    fun save(user: User): User = userRepository.save(user)

    fun findByEmail(email: String): User =
        userRepository.findByEmail(email).orElseThrow {
            BadRequestException("No user found with email $email")
        }

    fun getCurrentUser(): User {
        return userRepository.findById(getCurrentUserId()).get()
    }

    fun getCurrentUserId() = currentAuth().id

    private fun currentAuth() = SecurityContextHolder.getContext().authentication as AuthenticationToken
}
