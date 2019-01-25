package com.github.maly7.service

import com.github.maly7.clients.AuthenticationClient
import com.github.maly7.clients.ChatClient
import com.github.maly7.domain.UserAuth
import com.github.maly7.domain.UserResponse
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
