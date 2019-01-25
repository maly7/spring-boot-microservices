package com.github.maly7.clients

import com.github.maly7.domain.AuthToken
import com.github.maly7.domain.UserAuth
import com.github.maly7.domain.UserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "authentication-service", url = "\${auth.url}")
interface AuthenticationClient {

    @PostMapping("/user")
    fun createUser(userAuth: UserAuth): UserResponse

    @PostMapping("/login")
    fun login(userAuth: UserAuth): AuthToken
}
