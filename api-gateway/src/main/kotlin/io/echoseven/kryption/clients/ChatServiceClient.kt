package io.echoseven.kryption.clients

import io.echoseven.kryption.domain.UserResponse
import io.echoseven.kryption.domain.UserSignup
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "chat-service")
interface ChatServiceClient {

    @PostMapping("/user")
    fun createUser(userSignup: UserSignup): UserResponse
    
}
