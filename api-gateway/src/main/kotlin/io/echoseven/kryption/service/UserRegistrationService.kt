package io.echoseven.kryption.service

import io.echoseven.kryption.clients.ChatClient
import io.echoseven.kryption.domain.UserSignup
import org.springframework.stereotype.Service

@Service
class UserRegistrationService(private val chatClient: ChatClient) {
    fun registerUser(userSignup: UserSignup) = chatClient.createUser(userSignup)
}
