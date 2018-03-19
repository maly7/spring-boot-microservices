package io.echoseven.kryption.domain

import javax.validation.constraints.NotBlank

data class UserSignup(var id: String? = null,
                      @NotBlank var email: String? = null,
                      @NotBlank var password: String? = null)
