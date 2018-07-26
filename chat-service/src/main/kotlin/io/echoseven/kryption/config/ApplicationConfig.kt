package io.echoseven.kryption.config

import com.netflix.discovery.DiscoveryClient
import io.echoseven.kryption.commons.discovery.SSLConfiguredDiscoveryClientOptionalArgs
import io.echoseven.kryption.properties.MessagingProperties
import io.echoseven.kryption.service.UserService
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.InitializingBean
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
}
