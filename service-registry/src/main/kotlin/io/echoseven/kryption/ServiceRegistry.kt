package io.echoseven.kryption

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@EnableEurekaServer
@SpringBootApplication
class ServiceRegistry

fun main(args: Array<String>) {
    runApplication<ServiceRegistry>(*args)
}
