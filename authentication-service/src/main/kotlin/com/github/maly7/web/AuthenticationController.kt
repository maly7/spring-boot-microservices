package com.github.maly7.web

import com.github.maly7.domain.UserAccount
import com.github.maly7.exception.UserNotFoundException
import com.github.maly7.service.AuthenticationService
import com.github.maly7.web.resource.TokenResource
import com.github.maly7.web.resource.UserAccountResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping(path = ["/login"], consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun login(@Valid @RequestBody userAccount: UserAccount): TokenResource =
            TokenResource(authenticationService.authenticate(userAccount))

    @PostMapping(path = ["/authenticate"], consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun getUserFromToken(@RequestBody tokenResource: TokenResource): UserAccountResource {
        val user = authenticationService.authenticate(tokenResource.token).orElseThrow { UserNotFoundException("No User found for provided token") }
        return UserAccountResource(user)
    }

    @GetMapping(path = ["/authenticate"], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun getUserFromToken(@RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) token: String): UserAccountResource {
        val user = authenticationService.authenticate(token).orElseThrow { UserNotFoundException("No User found for provided token") }
        return UserAccountResource(user)
    }
}
