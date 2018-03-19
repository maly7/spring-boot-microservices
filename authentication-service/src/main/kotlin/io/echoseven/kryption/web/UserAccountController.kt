package io.echoseven.kryption.web

import io.echoseven.kryption.data.UserAccountRepository
import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.web.resource.UserAccountResource
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class UserAccountController(private val userAccountRepository: UserAccountRepository) {

    @PostMapping(value = ["/user"], consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun createUser(@Valid @RequestBody userAccount: UserAccount): UserAccountResource =
            UserAccountResource(userAccountRepository.save(userAccount))

}
