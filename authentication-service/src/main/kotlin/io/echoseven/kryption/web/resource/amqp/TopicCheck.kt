package io.echoseven.kryption.web.resource.amqp

import com.fasterxml.jackson.annotation.JsonProperty

class TopicCheck(@JsonProperty("routing_key") var routingKey: String) : Check()
