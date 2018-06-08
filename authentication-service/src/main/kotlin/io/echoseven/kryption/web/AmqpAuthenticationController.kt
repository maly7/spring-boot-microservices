package io.echoseven.kryption.web

import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.service.AuthenticationService
import io.echoseven.kryption.service.UserAccountService
import io.echoseven.kryption.web.resource.amqp.ALLOW_ACCESS
import io.echoseven.kryption.web.resource.amqp.DENY_ACCESS
import io.echoseven.kryption.web.resource.amqp.ResourceCheck
import io.echoseven.kryption.web.resource.amqp.TopicCheck
import io.echoseven.kryption.web.resource.amqp.VirtualHostCheck
import io.jsonwebtoken.MalformedJwtException
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
@RequestMapping(path = ["/amqp"])
class AmqpAuthenticationController(
    val authenticationService: AuthenticationService,
    val userAccountService: UserAccountService
) {
    private val log = LoggerFactory.getLogger(AmqpAuthenticationController::class.java)

    private val supportedResources = listOf("queue")

    @PostMapping(path = ["/user"], consumes = [MediaType.ALL_VALUE])
    fun user(@RequestParam username: String, @RequestParam password: String): String {
        log.debug("Attempting to authenticate user [{}] to amqp", username)

        val userOpt: Optional<UserAccount>

        try {
            userOpt = authenticationService.authenticate(password)
        } catch (e: MalformedJwtException) {
            return DENY_ACCESS
        }

        if (userOpt.isPresent && userOpt.get().email == username) {
            log.debug("Successfully authenticated [{}], granting amqp access", username)
            return ALLOW_ACCESS
        }

        log.warn("No user found for [{}], denying access", username)
        return DENY_ACCESS
    }

    @PostMapping(path = ["/vhost"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun vhost(@RequestParam check: VirtualHostCheck) = authVhost(check)

    @PostMapping(path = ["/vhost"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun vhost(@RequestParam username: String, @RequestParam vhost: String) =
        authVhost(VirtualHostCheck(username, vhost))

    @PostMapping(path = ["/resource"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun resource(@RequestBody check: ResourceCheck) = authResource(check)

    @PostMapping(path = ["/resource"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun resource(@RequestParam resource: String, @RequestParam name: String, @RequestParam permission: String, @RequestParam username: String, @RequestParam vhost: String) =
        authResource(ResourceCheck(resource, name, permission, username, vhost))

    @PostMapping(path = ["/topic"], consumes = [MediaType.ALL_VALUE])
    fun topic(@RequestBody check: TopicCheck): String {
        log.warn("Denying topic access for [{}], this is unsupported", check)
        return DENY_ACCESS
    }

    private fun authResource(check: ResourceCheck): String {
        log.debug("Checking access to resource [{}]", check)

        if (!supportedResources.contains(check.resource)) {
            log.warn(
                "Denying access to resource [{}], due to unsupported resource type. Current resource types are [{}]",
                check,
                supportedResources
            )
            return DENY_ACCESS
        }

        val userOpt = userAccountService.getByEmail(check.username)

        if (userOpt.isPresent && check.name.contains(userOpt.get().id.toString(), true)) {
            log.debug("Granting access to resource [{}]", check)
            return ALLOW_ACCESS
        }

        log.warn("Denying resource access for [{}], either user could not be found or it wasn't their queue", check)
        return DENY_ACCESS
    }

    private fun authVhost(check: VirtualHostCheck): String {
        if (check.username.isEmpty() || check.vhost.isEmpty()) {
            log.warn("Denying access to vhost [{}] for empty username or vhost field", check)
            return DENY_ACCESS
        }

        log.debug("Allowing access to vhost [{}] request", check)
        return ALLOW_ACCESS
    }
}
