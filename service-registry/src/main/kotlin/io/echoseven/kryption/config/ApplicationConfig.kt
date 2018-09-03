package io.echoseven.kryption.config

import com.netflix.discovery.DiscoveryClient
import io.echoseven.kryption.commons.discovery.SSLConfiguredDiscoveryClientOptionalArgs
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {

    @Bean
    fun discoveryClientOptionalArgs(): DiscoveryClient.DiscoveryClientOptionalArgs =
        SSLConfiguredDiscoveryClientOptionalArgs()
}
