package com.github.maly7.web.resource.amqp

class ResourceCheck(var resource: String, var name: String, var permission: String, username: String, vhost: String) :
    Check(username, vhost) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ResourceCheck

        if (resource != other.resource) return false
        if (name != other.name) return false
        if (permission != other.permission) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + resource.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + permission.hashCode()
        return result
    }

    override fun toString(): String {
        return "ResourceCheck(resource='$resource', name='$name', permission='$permission', username= '$username', vhost='$vhost')"
    }
}
