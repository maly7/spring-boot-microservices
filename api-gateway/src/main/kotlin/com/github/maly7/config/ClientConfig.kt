package com.github.maly7.config

import com.netflix.discovery.DiscoveryClient
import feign.codec.ErrorDecoder
import com.github.maly7.clients.error.ClientErrorDecoder
import com.github.maly7.commons.client.TrustingSSLOkHttpClientFactory
import com.github.maly7.commons.discovery.SSLConfiguredDiscoveryClientOptionalArgs
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
    fun okHttpClient(builder: OkHttpClient.Builder): OkHttpClient =
        builder.build()

    @Bean
    fun okHttpClientFactory(env: Environment, builder: OkHttpClient.Builder): OkHttpClientFactory {
        val trustStoreLocation = env.getRequiredProperty("javax.net.ssl.trustStore")
        val trustStorePassword = env.getRequiredProperty("javax.net.ssl.trustStorePassword")
        return TrustingSSLOkHttpClientFactory(builder, trustStoreLocation, trustStorePassword)
    }
}
