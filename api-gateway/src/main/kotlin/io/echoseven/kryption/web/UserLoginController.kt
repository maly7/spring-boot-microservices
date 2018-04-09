package io.echoseven.kryption.web

import io.echoseven.kryption.domain.UserAuth
import io.echoseven.kryption.domain.UserResponse
import io.echoseven.kryption.service.UserLoginService
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserLoginController(val userLoginService: UserLoginService) {

    @PostMapping(path = ["/login"], consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun login(@RequestBody userAuth: UserAuth): UserResponse = userLoginService.login(userAuth)
}
