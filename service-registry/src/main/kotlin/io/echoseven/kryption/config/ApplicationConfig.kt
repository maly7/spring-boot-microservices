package io.echoseven.kryption.config

import com.netflix.discovery.DiscoveryClient
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class ApplicationConfig {

    @Bean
    fun discoveryClientOptionalArgs(): DiscoveryClient.DiscoveryClientOptionalArgs {
        val args = DiscoveryClient.DiscoveryClientOptionalArgs()
        args.setSSLContext(
            SSLContextBuilder.create()
                .loadTrustMaterial(File("/app/trusts.jks"), "secret".toCharArray(), TrustSelfSignedStrategy()).build()
        )
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
