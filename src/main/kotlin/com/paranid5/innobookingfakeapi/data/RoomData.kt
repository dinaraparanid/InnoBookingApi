package com.paranid5.innobookingfakeapi.data

import kotlinx.serialization.Serializable

@Serializable
data class RoomData(val name: String, val id: String, val type: String, val capacity: Int)