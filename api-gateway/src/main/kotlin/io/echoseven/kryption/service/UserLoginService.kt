package io.echoseven.kryption.service

import io.echoseven.kryption.clients.AuthenticationClient
import io.echoseven.kryption.clients.ChatClient
import io.echoseven.kryption.domain.UserAuth
import io.echoseven.kryption.domain.UserResponse
import org.springframework.stereotype.Service

@Service
class UserLoginService(
    private val chatClient: ChatClient,
    private val authenticationClient: AuthenticationClient
) {

    fun login(userAuth: UserAuth): UserResponse {
        val token = authenticationClient.login(userAuth).token
        val user = chatClient.getUser(token)

        user.token = token
        user.id = ""

        return user
    }
}
