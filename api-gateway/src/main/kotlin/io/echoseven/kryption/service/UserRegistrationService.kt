package io.echoseven.kryption.service

import io.echoseven.kryption.clients.AuthenticationClient
import io.echoseven.kryption.clients.ChatClient
import io.echoseven.kryption.domain.UserResponse
import io.echoseven.kryption.domain.UserSignup
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserRegistrationService(
    private val chatClient: ChatClient,
    private val authenticationClient: AuthenticationClient
) {

    private val log = LoggerFactory.getLogger(UserRegistrationService::class.java)

    fun registerUser(userSignup: UserSignup): UserResponse {
        val chatUser = chatClient.createUser(userSignup)
        log.debug("User [{}] successfully created in chat service", chatUser)

        userSignup.id = chatUser.id

        try {
            val authUser = authenticationClient.createUser(userSignup)
            log.debug("User [{}] successfully created in auth service", authUser)
            return authUser
        } catch (e: Exception) {
            log.error("Error occurred during creation of user account [{}] in auth service, attempting to rollback chat service user", userSignup.email)
            chatClient.deleteUser(chatUser.id)
            throw IllegalStateException("User Creation Failed")
        }
    }
}
