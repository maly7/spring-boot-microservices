package io.echoseven.kryption.web

import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.exception.UserNotFoundException
import io.echoseven.kryption.service.AuthenticationService
import io.echoseven.kryption.web.resource.TokenResource
import io.echoseven.kryption.web.resource.UserAccountResource
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
}
