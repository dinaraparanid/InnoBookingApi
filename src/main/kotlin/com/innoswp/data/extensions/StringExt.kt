package com.innoswp.data.extensions

import kotlinx.datetime.Instant

fun String.toInstant(): Instant {
    val (dateStr, timeStr) = split(";")
    return Instant.parse("$dateStr$timeStr:00Z")
}

fun String.toInstantOrNull() = runCatching(this::toInstant).getOrNull()