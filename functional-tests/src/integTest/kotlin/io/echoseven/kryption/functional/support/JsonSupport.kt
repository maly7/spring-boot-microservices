package io.echoseven.kryption.functional.support

import com.beust.klaxon.Klaxon

fun toJson(map: Map<String, String>): String = Klaxon().toJsonString(map)
