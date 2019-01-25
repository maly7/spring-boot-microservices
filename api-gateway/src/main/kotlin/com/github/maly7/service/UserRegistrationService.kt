package com.github.maly7.service

import com.github.maly7.clients.AuthenticationClient
import com.github.maly7.clients.ChatClient
import com.github.maly7.domain.UserResponse
import com.github.maly7.domain.UserAuth
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserRegistrationService(
    private val chatClient: ChatClient,
    private val authenticationClient: AuthenticationClient
) {

    private val log = LoggerFactory.getLogger(UserRegistrationService::class.java)

    fun registerUser(userAuth: UserAuth): UserResponse {
        val chatUser = chatClient.createUser(userAuth)
        log.debug("User [{}] successfully created in chat service", chatUser)

        userAuth.id = chatUser.id

        try {
            val authUser = authenticationClient.createUser(userAuth)
            log.debug("User [{}] successfully created in auth service", authUser)
            return authUser
        } catch (e: Exception) {
            log.error("Error occurred during creation of user account [{}] in auth service, attempting to rollback chat service user", userAuth.email)
            chatClient.deleteUser(chatUser.id)
            throw IllegalStateException("User Creation Failed", e)
        }
    }
}
