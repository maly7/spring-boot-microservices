package com.github.maly7.service

import com.github.maly7.domain.UserAccount
import com.github.maly7.web.resource.amqp.ALLOW_ACCESS
import com.github.maly7.web.resource.amqp.DENY_ACCESS
import com.github.maly7.web.resource.amqp.ResourceCheck
import com.github.maly7.web.resource.amqp.VirtualHostCheck
import io.jsonwebtoken.MalformedJwtException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class AmqpAuthenticationService(
    val authenticationService: AuthenticationService,
    val userAccountService: UserAccountService
) {
    private val log = LoggerFactory.getLogger(AmqpAuthenticationService::class.java)
    private val supportedResources = listOf("queue")

    fun user(username: String, password: String): String {
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

    fun resource(check: ResourceCheck): String {
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

    fun vhost(check: VirtualHostCheck): String {
        if (check.username.isEmpty() || check.vhost.isEmpty()) {
            log.warn("Denying access to vhost [{}] for empty username or vhost field", check)
            return DENY_ACCESS
        }

        log.debug("Allowing access to vhost [{}] request", check)
        return ALLOW_ACCESS
    }
}
