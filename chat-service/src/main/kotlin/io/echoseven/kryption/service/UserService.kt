package io.echoseven.kryption.service

import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.exception.BadRequestException
import io.echoseven.kryption.exception.NotFoundException
import io.echoseven.kryption.security.AuthenticationToken
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository, val queueService: QueueService) {
    private val log = LoggerFactory.getLogger(UserService::class.java)

    fun create(user: User): User {
        log.debug("Creating User [{}]", user)
        val createdUser = userRepository.insert(user)
        queueService.createUserQueue(createdUser.id!!)
        return createdUser
    }

    fun get(id: String): User =
        userRepository.findById(id).orElseThrow { NotFoundException("No User Found with id $id") }

    fun save(user: User): User = userRepository.save(user)

    fun findByEmail(email: String): User =
        userRepository.findByEmail(email).orElseThrow {
            BadRequestException("No user found with email $email")
        }

    fun delete(id: String) {
        log.info("Deleting User with id [{}]", id)
        userRepository.deleteById(id)
    }

    fun deleteAll() = userRepository.deleteAll()

    fun getCurrentUser(): User {
        val authId = getCurrentUserId()
        log.debug("Fetching user data for [{}]", authId)
        return userRepository.findById(authId).orElseThrow {
            NotFoundException("A User could not be found for the current user, this should never happen")
        }
    }

    fun getCurrentUserId() = currentAuth().id

    private fun currentAuth() = SecurityContextHolder.getContext().authentication as AuthenticationToken
}
