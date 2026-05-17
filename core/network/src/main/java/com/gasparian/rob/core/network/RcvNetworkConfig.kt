package com.gasparian.rob.core.network

data class RcvNetworkConfig(
    val baseUrl: String,
    val requestTimeoutMillis: Long = 15_000,
    val connectTimeoutMillis: Long = 10_000,
    val socketTimeoutMillis: Long = 15_000,
)
