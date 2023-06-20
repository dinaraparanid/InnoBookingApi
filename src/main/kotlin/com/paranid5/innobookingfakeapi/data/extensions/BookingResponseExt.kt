package com.paranid5.innobookingfakeapi.data.extensions

import com.paranid5.innobookingfakeapi.data.BookResponse

inline val BookResponse.bookingMessage
    get() = """
        Successfully booked!
        Booking id: $id;
        Event title: $title
        Start: $start
        End: $end
        Room: ${room.name}
    """.trimIndent()