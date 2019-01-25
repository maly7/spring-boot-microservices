package com.github.maly7.web.resource.amqp

abstract class Check(var username: String = "", var vhost: String = "") {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Check

        if (username != other.username) return false
        if (vhost != other.vhost) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + vhost.hashCode()
        return result
    }

    override fun toString(): String {
        return "Check(username='$username', vhost='$vhost')"
    }
}
