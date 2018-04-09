package io.echoseven.kryption.clients

import io.echoseven.kryption.domain.AuthToken
import io.echoseven.kryption.domain.UserAuth
import io.echoseven.kryption.domain.UserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "authentication-service")
interface AuthenticationClient {

    @PostMapping("/user")
    fun createUser(userAuth: UserAuth): UserResponse

    @PostMapping("/login")
    fun login(userAuth: UserAuth): AuthToken
}
