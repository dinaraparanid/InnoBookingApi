package com.paranid5.innobookingfakeapi.data.extensions

import com.paranid5.innobookingfakeapi.data.Room

inline val List<Room>.joinedString
    get() = joinToString(separator = "\n") { "Room: ${it.name}; id: ${it.id}; capacity: ${it.capacity}" }