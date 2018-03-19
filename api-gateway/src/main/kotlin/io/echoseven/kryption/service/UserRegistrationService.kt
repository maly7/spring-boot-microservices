package io.echoseven.kryption.service

import io.echoseven.kryption.clients.ChatServiceClient
import io.echoseven.kryption.domain.UserSignup
import org.springframework.stereotype.Service

@Service
class UserRegistrationService(private val chatServiceClient: ChatServiceClient) {
    fun registerUser(userSignup: UserSignup) = chatServiceClient.createUser(userSignup)
}
