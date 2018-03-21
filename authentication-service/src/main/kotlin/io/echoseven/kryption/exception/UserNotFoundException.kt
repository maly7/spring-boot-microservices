package io.echoseven.kryption.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UserNotFoundException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace)
}
