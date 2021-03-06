package com.github.maly7.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "authentication-service", url = "\${auth.url}")
interface AuthenticationClient {

    @GetMapping("/authenticate")
    fun authenticate(@RequestHeader(name = HttpHeaders.AUTHORIZATION) token: String): AuthUser
}
