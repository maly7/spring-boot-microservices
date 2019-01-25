package com.github.maly7.config

import com.netflix.discovery.DiscoveryClient
import com.github.maly7.auditing.LocalDateTimeProvider
import com.github.maly7.commons.discovery.SSLConfiguredDiscoveryClientOptionalArgs
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
    fun signingKey(signatureAlgorithm: SignatureAlgorithm, tokenSettings: TokenSettings): Key {
        val bytes = IOUtils.toByteArray(ClassPathResource(tokenSettings.secret).inputStream)
        val keySpec = PKCS8EncodedKeySpec(bytes)
        return KeyFactory.getInstance(signatureAlgorithm.familyName).generatePrivate(keySpec)
    }

    @Bean
    fun signatureAlgorithm(): SignatureAlgorithm = SignatureAlgorithm.RS256

    @Bean
    fun discoveryClientOptionalArgs(): DiscoveryClient.DiscoveryClientOptionalArgs =
        SSLConfiguredDiscoveryClientOptionalArgs()
}
