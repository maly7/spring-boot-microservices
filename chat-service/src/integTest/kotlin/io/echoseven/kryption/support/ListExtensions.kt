package io.echoseven.kryption.support

fun List<LinkedHashMap<*, *>>.containsValue(propertyName: String, value: Any) = this.any { it[propertyName] == value }

fun List<LinkedHashMap<*, *>>.countValue(propertyName: String, value: Any) = this.count { it[propertyName] == value }
