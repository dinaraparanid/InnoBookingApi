package com.innoswp.data.extensions

import com.innoswp.data.BookResponse

inline val BookResponse.bookingMessage
    get() = """
        Successfully booked!
        Booking id: $id;
        Event title: $title
        Start: $start
        End: $end
        Room: ${room.name}
    """.trimIndent()