package com.github.maly7.web.resource

import com.github.maly7.domain.UserAccount

data class UserAccountResource(val id: String, val email: String, val isVerified: Boolean) {
    constructor(userAccount: UserAccount) : this(userAccount.id!!, userAccount.email!!, userAccount.isVerified)
}
