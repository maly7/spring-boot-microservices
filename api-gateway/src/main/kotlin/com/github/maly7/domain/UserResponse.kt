package com.github.maly7.domain

data class UserResponse(
    var email: String? = null,
    var id: String? = null,
    var onlineStatus: Boolean = false,
    var profileImageUrl: String = "noPhoto",
    var name: String = "Tap to update your name",
    var token: String = ""
)
