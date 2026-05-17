package com.gasparian.rob.core.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

class RcvNetworkResponse internal constructor(
    @PublishedApi internal val response: HttpResponse,
) {
    suspend inline fun <reified T> body(): T = response.body()
}
