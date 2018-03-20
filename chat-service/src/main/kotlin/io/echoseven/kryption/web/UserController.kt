package io.echoseven.kryption.web

import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.domain.User
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class UserController(private val userRepository: UserRepository) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = ["/user"], consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun createUser(@Valid @RequestBody user: User): User = userRepository.save(user)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/user/{id}")
    fun deleteUser(@PathVariable id: String) = userRepository.deleteById(id)

    // TODO: remove with #19
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/user")
    fun deleteAllUsers(): Unit = userRepository.deleteAll()

}
