package io.echoseven.kryption.functional.support

import com.beust.klaxon.Klaxon

fun toJson(map: Map<String, String>): String = Klaxon().toJsonString(map)

fun userRegistrationJson(email: String, password: String): String {
    val user = mapOf("email" to email, "password" to password)
    return toJson(user)
}
