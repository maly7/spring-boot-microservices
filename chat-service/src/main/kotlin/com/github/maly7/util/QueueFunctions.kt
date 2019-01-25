package com.github.maly7.util

import com.github.maly7.domain.User

fun userQueueId(user: User): String {
    val id = user.id ?: throw IllegalArgumentException("A User cannot have a null id")
    return userQueueId(id)
}

fun userQueueId(id: String) = "user.$id"
