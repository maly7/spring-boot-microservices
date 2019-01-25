package com.github.maly7.config

import com.netflix.discovery.DiscoveryClient
import com.github.maly7.commons.client.TrustingSSLOkHttpClientFactory
import com.github.maly7.commons.discovery.SSLConfiguredDiscoveryClientOptionalArgs
import com.github.maly7.properties.MessagingProperties
import com.github.maly7.service.UserService
import okhttp3.OkHttpClient
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.InitializingBean
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.data.domain.AuditorAware
import org.springframework.web.client.RestTemplate
import java.util.Optional

@Configuration
class ApplicationConfig {

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun userExchange(messagingProperties: MessagingProperties) = DirectExchange(messagingProperties.userExchange)

    @Bean
    fun exchangeCreator(amqpAdmin: AmqpAdmin, userExchange: Exchange): InitializingBean =
        InitializingBean {
            amqpAdmin.declareExchange(userExchange)
        }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
        return rabbitTemplate
    }

    @Bean
    fun auditorProvider(userService: UserService): AuditorAware<String> {
        return AuditorAware<String> {
            Optional.of(userService.getCurrentUserId())
        }
    }

    @Bean
    fun discoveryClientOptionalArgs(): DiscoveryClient.DiscoveryClientOptionalArgs =
        SSLConfiguredDiscoveryClientOptionalArgs()

    @Bean
    fun okHttpClient(builder: OkHttpClient.Builder): OkHttpClient =
        builder.build()

    @Bean
    @Profile("cloud")
    fun okHttpClientFactory(env: Environment, builder: OkHttpClient.Builder): OkHttpClientFactory {
        val trustStoreLocation = env.getRequiredProperty("javax.net.ssl.trustStore")
        val trustStorePassword = env.getRequiredProperty("javax.net.ssl.trustStorePassword")
        return TrustingSSLOkHttpClientFactory(builder, trustStoreLocation, trustStorePassword)
    }
}
