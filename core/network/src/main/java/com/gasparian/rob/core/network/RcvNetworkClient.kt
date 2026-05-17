package com.gasparian.rob.core.network

interface RcvNetworkClient {
    suspend fun <T> get(
        path: String,
        responseMapper: suspend RcvNetworkResponse.() -> T,
    ): RcvNetworkResult<T>
}
