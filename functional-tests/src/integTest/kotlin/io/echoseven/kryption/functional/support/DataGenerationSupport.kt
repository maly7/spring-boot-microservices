package io.echoseven.kryption.functional.support

import java.util.UUID

fun email() = "${UUID.randomUUID()}@email.com"

fun password() = "${UUID.randomUUID()}"
