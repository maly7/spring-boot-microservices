package io.echoseven.kryption.config

import feign.codec.ErrorDecoder
import io.echoseven.kryption.clients.error.ClientErrorDecoder
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestClientConfig {

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun errorDecoder(): ErrorDecoder = ClientErrorDecoder()
}
