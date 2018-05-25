package io.echoseven.kryption.web

import io.echoseven.kryption.domain.User
import io.echoseven.kryption.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController(val userService: UserService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun createUser(@Valid @RequestBody user: User): User = userService.create(user)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String) = userService.delete(id)

    // TODO: remove with #19
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun deleteAllUsers(): Unit = userService.deleteAll()

    @GetMapping
    fun getUser(): User = userService.getCurrentUser()
}
