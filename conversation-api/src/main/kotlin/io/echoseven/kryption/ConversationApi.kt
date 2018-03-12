package io.echoseven.kryption

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConversationApi

fun main(args: Array<String>) {
    runApplication<ConversationApi>(*args)
}