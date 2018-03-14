package io.echoseven.kryption

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class AuthenticationApp

fun main(args: Array<String>) {
    runApplication<AuthenticationApp>(*args)
}
