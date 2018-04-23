package io.echoseven.kryption.web.resource

import javax.validation.constraints.NotBlank

data class UserSearchRequest(@NotBlank var email: String? = null)
