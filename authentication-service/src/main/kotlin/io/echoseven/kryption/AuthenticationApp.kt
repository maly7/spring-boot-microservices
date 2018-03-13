package io.echoseven.kryption

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthenticationApp

fun main(args: Array<String>) {
    runApplication<AuthenticationApp>(*args)
}
