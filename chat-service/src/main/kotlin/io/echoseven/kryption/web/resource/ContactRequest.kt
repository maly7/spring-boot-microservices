package io.echoseven.kryption.web.resource

import javax.validation.constraints.NotBlank

data class ContactRequest(@NotBlank var email: String? = null)
