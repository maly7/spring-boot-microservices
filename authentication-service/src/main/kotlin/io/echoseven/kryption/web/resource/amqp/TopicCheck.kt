package io.echoseven.kryption.web.resource.amqp

import com.fasterxml.jackson.annotation.JsonProperty

class TopicCheck(@JsonProperty("routing_key") var routingKey: String) : Check() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as TopicCheck

        if (routingKey != other.routingKey) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + routingKey.hashCode()
        return result
    }

    override fun toString(): String {
        return "TopicCheck(routingKey='$routingKey', username= '$username', vhost='$vhost')"
    }
}
