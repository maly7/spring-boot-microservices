package io.echoseven.kryption.web

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/amqp"])
class AmqpAuthenticationController {
    private val log = LoggerFactory.getLogger(AmqpAuthenticationController::class.java)


}
