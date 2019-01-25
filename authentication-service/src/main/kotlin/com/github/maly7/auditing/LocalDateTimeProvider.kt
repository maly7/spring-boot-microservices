package com.github.maly7.auditing

import org.springframework.data.auditing.DateTimeProvider
import java.time.LocalDateTime
import java.time.temporal.TemporalAccessor
import java.util.Optional

class LocalDateTimeProvider : DateTimeProvider {
    override fun getNow(): Optional<TemporalAccessor> {
        return Optional.of(LocalDateTime.now())
    }
}
