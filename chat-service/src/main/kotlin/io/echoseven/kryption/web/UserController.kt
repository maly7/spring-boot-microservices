package io.echoseven.kryption.web

import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.exception.NotFoundException
import io.echoseven.kryption.security.AuthenticationToken
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.security.core.context.SecurityContextHolder
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
class UserController(private val userRepository: UserRepository) {
    private val log = LoggerFactory.getLogger(UserController::class.java)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun createUser(@Valid @RequestBody user: User): User = userRepository.save(user)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String) = userRepository.deleteById(id)

    // TODO: remove with #19
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun deleteAllUsers(): Unit = userRepository.deleteAll()

    @GetMapping
    fun getUser(): User {
        val auth: AuthenticationToken = (SecurityContextHolder.getContext().authentication as AuthenticationToken?)!!
        log.debug("Fetching user data for [{}]", auth)
        return userRepository.findById(auth.id).orElseThrow {
            NotFoundException("A User could not be found for the current user, this should never happen")
        }
    }
}
