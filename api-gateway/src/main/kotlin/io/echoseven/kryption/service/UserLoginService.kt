package io.echoseven.kryption.service

import io.echoseven.kryption.clients.AuthenticationClient
import io.echoseven.kryption.clients.ChatClient
import io.echoseven.kryption.domain.UserAuth
import io.echoseven.kryption.domain.UserResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserLoginService(
    private val chatClient: ChatClient,
    private val authenticationClient: AuthenticationClient
) {
    private val log = LoggerFactory.getLogger(UserLoginService::class.java)

    fun login(userAuth: UserAuth): UserResponse {
        val token = authenticationClient.login(userAuth).token
        log.debug("Successfully authenticated user [{}] and received token [{}]", userAuth.email, token)

        val user = chatClient.getUser(token)
        log.debug("Fetched User [{}] from chat service", user)

        user.token = token
        user.id = ""

        return user
    }
}
