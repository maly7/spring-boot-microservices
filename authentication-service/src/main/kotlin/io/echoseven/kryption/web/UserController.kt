package io.echoseven.kryption.web

import io.echoseven.kryption.data.UserAccountRepository
import io.echoseven.kryption.domain.UserAccount
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userAccountRepository: UserAccountRepository) {

    @PostMapping(value = ["/user"], consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun createUser(@RequestBody userAccount: UserAccount): UserAccount = userAccountRepository.save(userAccount)

}
