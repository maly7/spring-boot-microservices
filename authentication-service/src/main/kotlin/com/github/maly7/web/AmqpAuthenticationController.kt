package com.github.maly7.web

import com.github.maly7.service.AmqpAuthenticationService
import com.github.maly7.web.resource.amqp.DENY_ACCESS
import com.github.maly7.web.resource.amqp.ResourceCheck
import com.github.maly7.web.resource.amqp.TopicCheck
import com.github.maly7.web.resource.amqp.VirtualHostCheck
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/amqp"])
class AmqpAuthenticationController(val amqpAuthenticationService: AmqpAuthenticationService) {
    private val log = LoggerFactory.getLogger(AmqpAuthenticationController::class.java)

    @PostMapping(path = ["/user"], consumes = [MediaType.ALL_VALUE])
    fun user(@RequestParam username: String, @RequestParam password: String) =
        amqpAuthenticationService.user(username, password)

    @PostMapping(path = ["/vhost"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun vhost(@RequestBody check: VirtualHostCheck) =
        amqpAuthenticationService.vhost(check)

    @PostMapping(path = ["/vhost"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun vhost(@RequestParam username: String, @RequestParam vhost: String) =
        amqpAuthenticationService.vhost(VirtualHostCheck(username, vhost))

    @PostMapping(path = ["/resource"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun resource(@RequestBody check: ResourceCheck) =
        amqpAuthenticationService.resource(check)

    @PostMapping(path = ["/resource"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun resource(
        @RequestParam resource: String,
        @RequestParam name: String,
        @RequestParam permission: String,
        @RequestParam username: String,
        @RequestParam vhost: String
    ) = amqpAuthenticationService.resource(ResourceCheck(resource, name, permission, username, vhost))

    @PostMapping(path = ["/topic"], consumes = [MediaType.ALL_VALUE])
    fun topic(@RequestBody check: TopicCheck): String {
        log.warn("Denying topic access for [{}], this is unsupported", check)
        return DENY_ACCESS
    }
}
