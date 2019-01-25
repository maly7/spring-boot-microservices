package com.github.maly7.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("app-secrets.jwt")
class TokenSettings(var secret: String = "keys/private_key.der")
