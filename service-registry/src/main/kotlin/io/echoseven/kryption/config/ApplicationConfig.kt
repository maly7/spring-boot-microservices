package io.echoseven.kryption.config

import com.netflix.discovery.DiscoveryClient
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {

    @Bean
    fun discoveryClientOptionalArgs(): DiscoveryClient.DiscoveryClientOptionalArgs {
        val args = DiscoveryClient.DiscoveryClientOptionalArgs()
        args.setEurekaJerseyClient(
            EurekaJerseyClientImpl.EurekaJerseyClientBuilder()
                .withSystemSSLConfiguration()
                .withClientName("registry-client")
                .withMaxConnectionsPerHost(10)
                .withMaxTotalConnections(10)
                .build()
        )

        return args
    }
}
