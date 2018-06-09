package io.echoseven.kryption.functional.support

const val WEBSOCKET_URI = "ws://localhost:8080/realtime/chat"

fun queueName(userId: String) = "/queue/user.$userId"
