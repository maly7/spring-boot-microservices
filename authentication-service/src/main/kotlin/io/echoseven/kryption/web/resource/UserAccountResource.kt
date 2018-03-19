package io.echoseven.kryption.web.resource

import io.echoseven.kryption.domain.UserAccount

data class UserAccountResource(val id: String, val email: String, val isVerified: Boolean) {
    constructor(userAccount: UserAccount) : this(userAccount.id!!, userAccount.email!!, userAccount.isVerified)
}
