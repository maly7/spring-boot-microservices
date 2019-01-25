package com.github.maly7.clients

import com.github.maly7.domain.UserAuth
import com.github.maly7.domain.UserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "chat-service", url = "\${chat.url}")
interface ChatClient {

    @PostMapping("/user")
    fun createUser(userAuth: UserAuth): UserResponse

    @GetMapping("/user")
    fun getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) token: String): UserResponse

    @DeleteMapping("/user/{id}")
    fun deleteUser(@PathVariable id: String?)
}
