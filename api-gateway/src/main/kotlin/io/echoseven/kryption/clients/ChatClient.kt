package io.echoseven.kryption.clients

import io.echoseven.kryption.domain.UserAuth
import io.echoseven.kryption.domain.UserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "chat-service")
interface ChatClient {

    @PostMapping("/user")
    fun createUser(userAuth: UserAuth): UserResponse

    @GetMapping("/user")
    fun getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) token: String): UserResponse

    @DeleteMapping("/user/{id}")
    fun deleteUser(@PathVariable id: String?)
}
