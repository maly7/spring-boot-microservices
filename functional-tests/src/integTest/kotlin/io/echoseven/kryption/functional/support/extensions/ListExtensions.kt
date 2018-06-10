package io.echoseven.kryption.functional.support.extensions

const val STATUS_PROP = "status"

// TODO: move to a lib different project
fun List<LinkedHashMap<*, *>>.containsValue(propertyName: String, value: Any) = this.any { it[propertyName] == value }

fun List<LinkedHashMap<*, *>>.containsStatus(value: Any) = containsValue(STATUS_PROP, value)

fun List<LinkedHashMap<*, *>>.containsNewMessage() = containsStatus("NEW_MESSAGE")

fun List<LinkedHashMap<*, *>>.containsNewConversation() = containsStatus("NEW_CONVERSATION")

fun List<LinkedHashMap<*, *>>.containsDeleteConversation() = containsStatus("DELETE_CONVERSATION")

fun List<LinkedHashMap<*, *>>.containsDeleteMessage() = containsStatus("DELETE_MESSAGE")

fun List<LinkedHashMap<*, *>>.findValue(propertyName: String, value: Any) = this.find { it[propertyName] == value }

fun List<LinkedHashMap<*, *>>.findStatus(value: Any) = findValue(STATUS_PROP, value)

fun List<LinkedHashMap<*, *>>.findNewMessage() = findStatus("NEW_MESSAGE")

fun List<LinkedHashMap<*, *>>.findNewConversation() = findStatus("NEW_CONVERSATION")
