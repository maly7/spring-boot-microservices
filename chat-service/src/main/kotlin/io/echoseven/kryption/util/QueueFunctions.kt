package io.echoseven.kryption.util

import io.echoseven.kryption.domain.User

fun userQueueId(user: User): String {
    val id = user.id ?: throw IllegalArgumentException("A User cannot have a null id")
    return userQueueId(id)
}

fun userQueueId(id: String) = "user.$id"
