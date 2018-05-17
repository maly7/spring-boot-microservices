package io.echoseven.kryption.functional.support

fun messageJson(toId: String, message: String): String {
    val conversationMessage = mapOf("toId" to toId, "message" to message)
    return toJson(conversationMessage)
}
