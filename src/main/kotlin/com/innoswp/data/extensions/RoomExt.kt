package com.innoswp.data.extensions

import com.innoswp.data.Room

inline val List<Room>.joinedString
    get() = joinToString(separator = "\n") { "Room: ${it.name}; id: ${it.id}; capacity: ${it.capacity}" }