package io.echoseven.kryption.service

import io.echoseven.kryption.domain.UserSignup
import org.springframework.stereotype.Service

@Service
class UserRegistrationService {
    fun registerUser(userSignup: UserSignup) = userSignup
}
