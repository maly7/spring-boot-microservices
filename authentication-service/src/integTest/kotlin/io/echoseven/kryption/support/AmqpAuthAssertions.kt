package io.echoseven.kryption.support

import io.echoseven.kryption.web.resource.amqp.ALLOW_ACCESS
import io.echoseven.kryption.web.resource.amqp.DENY_ACCESS
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

fun ResponseEntity<String>.assertDeniedAccess() {
    assertEquals(HttpStatus.OK, this.statusCode, "The response should be 200 OK")
    assertEquals(DENY_ACCESS, this.body, "The user should be denied access")
}

fun ResponseEntity<String>.assertAllowedAccess() {
    assertEquals(HttpStatus.OK, this.statusCode, "The response should be 200 OK")
    assertEquals(ALLOW_ACCESS, this.body, "The user should be allowed access")
}
