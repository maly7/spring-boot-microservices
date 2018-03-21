package io.echoseven.kryption.web

import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.exception.UserNotFoundException
import io.echoseven.kryption.service.UserAccountService
import io.echoseven.kryption.web.resource.UserAccountResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class UserAccountController(private val userAccountService: UserAccountService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = ["/user"], consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun createUser(@Valid @RequestBody userAccount: UserAccount): UserAccountResource =
            UserAccountResource(userAccountService.create(userAccount))

    @GetMapping(path = ["/user/{token}"], consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun getUserFromToken(@PathVariable token: String): UserAccountResource {
        val user = userAccountService.getFromToken(token).orElseThrow { UserNotFoundException("No User found for provided token") }
        return UserAccountResource(user)
    }

    // TODO: remove with #19
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/user")
    fun deleteAllUsers(): Unit = userAccountService.deleteAllUsers()

}
