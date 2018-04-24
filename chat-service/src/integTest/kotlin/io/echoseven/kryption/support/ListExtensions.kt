package io.echoseven.kryption.support

fun List<LinkedHashMap<*, *>>.containsValue(propertyName: String, value: Any) = this.any { it[propertyName] == value }

fun List<LinkedHashMap<*, *>>.containsEmail(value: Any) = this.containsValue("email", value)

fun List<LinkedHashMap<*, *>>.countValue(propertyName: String, value: Any) = this.count { it[propertyName] == value }

fun List<LinkedHashMap<*, *>>.countEmail(value: Any) = this.countValue("email", value)
