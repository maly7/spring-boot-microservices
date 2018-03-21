package io.echoseven.kryption.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("kryption.jwt")
class TokenSettings(var secret: String = "keys/private_key.der")
