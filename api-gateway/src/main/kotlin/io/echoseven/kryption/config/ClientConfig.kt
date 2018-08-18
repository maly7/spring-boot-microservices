package io.echoseven.kryption.config

import com.netflix.discovery.DiscoveryClient
import feign.codec.ErrorDecoder
import io.echoseven.kryption.clients.error.ClientErrorDecoder
import io.echoseven.kryption.commons.client.CustomSSLOkHttpClientFactory
import io.echoseven.kryption.commons.discovery.SSLConfiguredDiscoveryClientOptionalArgs
import okhttp3.OkHttpClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.client.RestTemplate

@Configuration
class ClientConfig {

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun errorDecoder(): ErrorDecoder = ClientErrorDecoder()

    @Bean
    fun discoveryClientOptionalArgs(): DiscoveryClient.DiscoveryClientOptionalArgs =
        SSLConfiguredDiscoveryClientOptionalArgs()

    @Bean
    fun okHttpClientFactory(env: Environment, builder: OkHttpClient.Builder): OkHttpClientFactory =
        CustomSSLOkHttpClientFactory(builder, env.getProperty("TRUSTSTORE_PASSWORD")!!)
}
