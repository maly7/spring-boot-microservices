package com.github.maly7

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.crypto.MacProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import java.security.Key

class TestJwtConfig {

    @Bean
    @Primary
    fun signingKey(signatureAlgorithm: SignatureAlgorithm): Key = MacProvider.generateKey(signatureAlgorithm)

    @Bean
    @Primary
    fun signatureAlgorithm(): SignatureAlgorithm = SignatureAlgorithm.HS256
}
