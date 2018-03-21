package io.echoseven.kryption.config

import io.echoseven.kryption.auditing.LocalDateTimeProvider
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.commons.io.IOUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.security.Key
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec

@Configuration
class ApplicationConfig {
    @Bean
    fun dateTimeProvider(): DateTimeProvider = LocalDateTimeProvider()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun signingKey(signatureAlgorithm: SignatureAlgorithm): Key {
        val bytes = IOUtils.toByteArray(ClassPathResource("keys/private_key.der").inputStream)
        val keySpec = PKCS8EncodedKeySpec(bytes)
        return KeyFactory.getInstance(signatureAlgorithm.familyName).generatePrivate(keySpec)
    }

    @Bean
    fun signatureAlgorithm(): SignatureAlgorithm = SignatureAlgorithm.RS256
}
