package com.vito.core

data class User(
    val uid: String = "",
    val alias: String = "",
    val role: String = "client",
    val photoUrl: String? = null
)

data class Ride(
    val id: String = "",
    val pickup: String = "",
    val drop: String = "",
    val status: String = "pending",
    val fare: Int = 0
)

data class Driver(
    val id: String = "",
    val alias: String = "",
    val rating: Float = 5f,
    val vehicle: String = ""
)

object Constants {
    const val APP_NAME = "Vito"
    const val VERSION = "1.0.0"
}