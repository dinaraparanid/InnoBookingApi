package com.paranid5.innobookingfakeapi.data.exposed.rooms

import com.paranid5.innobookingfakeapi.data.RoomData

fun RoomDao.toRoomData() = RoomData(
    name = name,
    id = id.value,
    type = type,
    capacity = capacity
)