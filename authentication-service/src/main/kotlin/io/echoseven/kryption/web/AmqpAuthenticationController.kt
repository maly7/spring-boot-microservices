package io.echoseven.kryption.web

import io.echoseven.kryption.web.resource.amqp.ResourceCheck
import io.echoseven.kryption.web.resource.amqp.TopicCheck
import io.echoseven.kryption.web.resource.amqp.VirtualHostCheck
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/amqp"])
class AmqpAuthenticationController {
    private val log = LoggerFactory.getLogger(AmqpAuthenticationController::class.java)

    private val allowAccess = "allow"
    private val denyAccess = "deny"

    @RequestMapping(path = ["/user"], method = [GET, POST])
    fun user(@RequestParam username: String, @RequestParam password: String): String {
        log.debug("Attempting to authenticate user [{}] to amqp", username)
        // TODO: authenticate user
        return "$allowAccess administrator management"
    }

    @RequestMapping(path = ["/vhost"], method = [GET, POST])
    fun vhost(check: VirtualHostCheck): String {
        log.debug("Checking vhost access with [{}]", check)
        return allowAccess
    }

    @RequestMapping(path = ["/resource"], method = [GET, POST])
    fun resource(check: ResourceCheck): String {
        log.debug("Checking resource access with [{}]", check)
        // TODO: check user id with resource id
        return allowAccess
    }

    @RequestMapping(path = ["/topic"], method = [GET, POST])
    fun topic(check: TopicCheck): String {
        log.debug("Checking topic access with [{}]", check)
        return denyAccess
    }
}
