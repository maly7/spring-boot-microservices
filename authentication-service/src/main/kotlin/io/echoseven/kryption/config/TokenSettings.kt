package io.echoseven.kryption.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

@Component
@ConfigurationProperties("kryption.jwt")
class TokenSettings {
    var secret: String = "secret"
        set(value) {
            field = Base64.getEncoder().encode(value.toByteArray()).toString()
        }
}
