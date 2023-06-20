package com.innoswp.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class BookTimePeriod(val start: Instant, val end: Instant)
