package io.echoseven.kryption.config

import io.echoseven.kryption.security.TokenAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {
    @Bean
    fun tokenAuthenticationFilter(): TokenAuthenticationFilter {
        val tokenFilter = TokenAuthenticationFilter()
        tokenFilter.setCheckForPrincipalChanges(true)
        return tokenFilter
    }
}
