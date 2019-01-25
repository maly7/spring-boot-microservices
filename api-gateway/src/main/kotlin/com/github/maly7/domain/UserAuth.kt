package com.github.maly7.domain

import javax.validation.constraints.NotBlank

data class UserAuth(
    var id: String? = null,
    @NotBlank var email: String? = null,
    @NotBlank var password: String? = null
)
