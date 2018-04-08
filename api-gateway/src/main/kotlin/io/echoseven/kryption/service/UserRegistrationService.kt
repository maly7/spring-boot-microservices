package io.echoseven.kryption.service

import io.echoseven.kryption.clients.AuthenticationClient
import io.echoseven.kryption.clients.ChatClient
import io.echoseven.kryption.domain.UserRegistration
import io.echoseven.kryption.domain.UserResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserRegistrationService(
    private val chatClient: ChatClient,
    private val authenticationClient: AuthenticationClient
) {

    private val log = LoggerFactory.getLogger(UserRegistrationService::class.java)

    fun registerUser(userRegistration: UserRegistration): UserResponse {
        val chatUser = chatClient.createUser(userRegistration)
        log.debug("User [{}] successfully created in chat service", chatUser)

        userRegistration.id = chatUser.id

        val authUser = authenticationClient.createUser(userRegistration)
        log.debug("User [{}] successfully created in auth service", authUser)
        return authUser
    }
}
