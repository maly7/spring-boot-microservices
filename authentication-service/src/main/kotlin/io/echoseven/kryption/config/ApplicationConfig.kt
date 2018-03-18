package io.echoseven.kryption.config

import io.echoseven.kryption.auditing.LocalDateTimeProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider

@Configuration
class ApplicationConfig {
    @Bean
    fun dateTimeProvider(): DateTimeProvider = LocalDateTimeProvider()
}
