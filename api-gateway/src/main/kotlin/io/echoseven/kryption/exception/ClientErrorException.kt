package io.echoseven.kryption.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class ClientErrorException(status: HttpStatus, reason: String?) : ResponseStatusException(status, reason)
