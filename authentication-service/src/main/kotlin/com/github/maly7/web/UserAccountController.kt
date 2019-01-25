package com.github.maly7.web

import com.github.maly7.domain.UserAccount
import com.github.maly7.service.UserAccountService
import com.github.maly7.web.resource.UserAccountResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class UserAccountController(private val userAccountService: UserAccountService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = ["/user"], consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun createUser(@Valid @RequestBody userAccount: UserAccount): UserAccountResource =
        UserAccountResource(userAccountService.create(userAccount))

    // TODO: remove with #19
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/user")
    fun deleteAllUsers(): Unit = userAccountService.deleteAllUsers()
}
