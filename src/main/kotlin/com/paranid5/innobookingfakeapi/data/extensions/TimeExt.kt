package com.paranid5.innobookingfakeapi.data.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

inline val localZoneOffset: ZoneOffset
    get() = ZoneId.of(ZoneOffset.systemDefault().id).rules.getOffset(java.time.Instant.now())

inline val localTimeZone
    get() = TimeZone.currentSystemDefault()

fun Instant.toJavaLocalDateTime() = LocalDateTime.ofInstant(toJavaInstant(), localZoneOffset)

fun LocalDateTime.toKotlinInstant() = toInstant(localZoneOffset).toKotlinInstant()